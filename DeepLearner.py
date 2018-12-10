import numpy as np
import MBtable as MB

from random import randint

import time

from keras.models import Sequential
from keras.layers import Dense, Activation
from keras import losses

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
    return MB.table[A.upper()][B.upper()]

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

def makeModel(studentSkillsCount, lr=0.025):
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

skills = ["CS", "More CS"]*10
model = makeModel(len(skills))
data = genData(skills, n=10000)

sttime = time.time()
history = model.fit(data[0],data[1])
print("Training the model took: "+ str(time.time()-sttime))

#print(history.history)

print(model.predict(np.array([[5, *([1]*len(skills)),5, *([5]*len(skills))]])))
## TODO : actually attaching the model to the rest of the application
## TODO : convert the json into a proper database (to make the data quickly queryable)
## TODO : save the model?
## TODO : USE 8 BIT INTEGERS
