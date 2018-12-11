import numpy as np
import MBtable as MB

from random import randint

import time

from keras.models import Sequential
from keras.layers import Dense, Activation
from keras import losses, optimizers

from firebase import firebase

from flask import Flask, request

app = Flask(__name__)

rtdb = firebase.FirebaseApplication("https://teamup-224504.firebaseio.com", None)

"""
Our firebase data is in the form of
Member{
    Skills{
        skillname:rating
        ...
        },
    Personality: MyersBriggsCode,
    MyRatingsOfOtherMembers:{
        MemberID:rating
        ...
        }
    }
"""

def norm(x, min=1, max=5):
    """normalize x in [min,max] -> [-1,1]"""
    return ((2*(x-max))/(max-min))+1

def getPersonalityCompat(A,B):
    """returns a compatibility ranking [1,5],
    based on how well the two personality types interact"""
    try:
        return MB.table[A.upper()][B.upper()]
    except KeyError:
        return 2;

def makeData(membersDict):
    # TODO : keep this or not?
    # if so; actually implement it
    members = list(membersDict.keys())
    skilltags = list(membersDict[members[0]]["Skills"].keys())

def randRatings(numSkills):
    r = []
    for i in range(numSkills):
        r.append(randint(1,5))
    return r

personalityList = list(MB.table.keys())#keep the variable global to avoid converting the dict each time we perform a lookup
def randPersonality():
    return personalityList[randint(0,len(personalityList)-1)]

def estimate(a, b):
    skillSimilarity = np.sqrt(sum((a[1:] - b[1:])**2))
    personalityDisparity = (norm(a[0]) + norm(b[0])) * 5
    return  skillSimilarity + personalityDisparity#we take the square root to turn make the distribution normal instead of uniform

def genData(skillNames, n=1000):
    sttime = time.time()
    inputs = [] # list of Inputlists
    outputs =[] # list of expected outputs
    numskills = len(skillNames)
    
    ratingDomainMax = estimate(*np.array([[5, *([1]*numskills)],[5, *([5]*numskills)]]))
    ratingDomainMin = estimate(*np.array([[1, *([1]*numskills)],[1, *([1]*numskills)]]))
    
    for i in range(n):
        Apers = randPersonality()
        Bpers = randPersonality()
        a2b, b2a = getPersonalityCompat(Apers,Bpers), getPersonalityCompat(Bpers,Apers)
        inputList = np.array([[a2b, *randRatings(len(skillNames))],[b2a, *randRatings(len(skillNames))]])
        estimatedRating = estimate(*inputList)
        normRating = norm(estimatedRating, min=ratingDomainMin, max=ratingDomainMax)
        inputs.append(inputList)
        outputs.append(normRating)

    print("genData took: "+ str(time.time()-sttime))
    inputs = np.array(inputs)
    inputs = inputs.flatten().reshape(inputs.shape[0],(numskills+1)*2)
    return inputs, np.array(outputs)
        

"""
We are parsing the firebase data into student vectors to train our ANN.
The input is two student vectors made from selections of students who were in groups together. 

Input = [[AtoBPersonalityRating, Skill_1a, Skill_2a, Skill_3a, ... Skill_na],
         [BtoAPersonalityRating, Skill_1b, Skill_2b, Skill_3b, ... Skill_nb]]

NOTE : This is the operation of grouping student A with student B. This is not necessarily the same as grouping B with A

Next, we train the ANN on these pairings (X1,X2), and backpropogate based on how X1 rated X2.
In doing so, we train the ANN to _predict_ the ratings of any two future students.
The output is a single floating point number [1,5]; the prediction of how student X1 will rate X2.

NOTE : We generate a new model for each class, for multiple reasons:
    Reason 1: With each new class that's made, we may have more real data available to us to train our ANN with
    Reason 2: Each class may have different skills which the professor is interested in. This 
                will change the size of our input array - and therefore the structure of our ANN.

"""

def makeModel(studentSkillsCount, lr=0.05):
    n = studentSkillsCount + 1 # n is the row size (the number of skills + one, because we also include the personality compatibility.)
    model = Sequential()
    model.add(Dense(n*2, input_shape=(n*2,), kernel_initializer="he_normal"))
    model.add(Activation("tanh"))
    model.add(Dense(n*4, activation="tanh", kernel_initializer="he_normal"))
    model.add(Dense(1, kernel_initializer='he_normal'))
    model.add(Activation('tanh'))
    sgd = optimizers.SGD(lr=lr)
    model.compile(optimizer=sgd, loss=losses.mean_squared_error)
    return model

## TODO : USE 8 BIT INTEGERS

def isPaired(pairings, person):
    for pairing in pairings:
        if person in pairing:
            return True
    return False

gpc = getPersonalityCompat
def pair(A, B):
    return np.array([[gpc(A[2],B[2]), *A[1], gpc(B[2],A[2]), *B[1]]])

def makePairings(dbdata):
    skills = list(dbdata[0][1].keys())
    skills.remove("personality")
    students = []
    for datum in dbdata:
        uid = datum[0]
        skillvector = []
        for skill in skills:
            skillvector.append(datum[1][skill])

        students.append([uid, skillvector, datum[1]["personality"]])

        
    model = makeModel(len(skills))
    training_data = genData(skills, n=10000)

    sttime = time.time()
    history = model.fit(training_data[0],training_data[1], epochs=10, batch_size=32)
    print("Training the model took: "+ str(time.time()-sttime))

    matches = dict(map(lambda x:(x[0],[]),students))
    for personA in students:
        best_match = max(map(lambda personX: [model.predict(pair(personA, personX))[0][0],personA[0],personX[0]], [student for student in students if student[0] != personA[0]]), key=lambda _:_[0])
        matches[best_match[1]].append(best_match[2])
        matches[best_match[2]].append(best_match[1])

        students[:] = [student for student in students if not matches[student[0]]]
        
    #at this point, matches is a dictionary containing mappings from one student to another, s.t. each mapping is the best possible pairing
    return matches


def groupsFromMatches(matches):
    allmatches = list(matches.keys())
    groups = []
    for match in allmatches:
        groups.append({})
        current_group = [match, *matches[match]]
        for mem in current_group:
            groups[-1][mem] = True
        allmatches[:] = [m for m in allmatches if (not current_group.count(m))]
        
    return groups


@app.route("/", methods=["POST", "GET"])
def makeGroup(classUID):
    #classUID = request.arg.get("ClassUID", type=str)

    clss = rtdb.get("/Classes", classUID)

    dbdata = []

    #load all of the data for the current class
    for memberUID in clss["memberUIDs"].keys():
        memberSkills = rtdb.get("/Skills", memberUID)
        myersbrigs = rtdb.get("/Students/"+memberUID.split(":")[0],"personality")
        dbdata.append([memberUID,memberSkills["skills"]])
        dbdata[-1][1]["personality"] = myersbrigs

    #TODO : make groups of size N, as opposed to just pairings of size 2
    matches = makePairings(dbdata)
    groups = groupsFromMatches(matches)
    groupUIDs = {}
    #now we must create the group objects in firebase, and put the groupUID into the Member and Class objects.
    for group in groups:
        ret = rtdb.post("/Groups", {"name":"New Group", "members":groups[0], "classUID":classUID})
        rtdb.patch("/Groups/"+ret["name"], {"UID":ret["name"]})
        groupUIDs[ret["name"]] = True

        for memberUID in list(group.keys()):# update each member with their group ID
            rtdb.patch("/Members/"+memberUID+"/", {"groupUID":ret["name"]})
    #at this point, the groups are made, and we are just updating
    rtdb.patch("/Classes/"+classUID+"/groupUIDs", groupUIDs)

    #and we are clear
    return "abradacadarba"

    


