package wpi.jtkaplan.teamup.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.HashMap;

/**
 * Class model
 */
@IgnoreExtraProperties
public class Class extends DeclarativeElement {
    private String name;
    private String professorUID;
    private HashMap<String, Boolean> memberUIDs = new HashMap<String, Boolean>(); // Members are students that belong to this class. This allows students to quickly/neatly index many skills for many classes
    private HashMap<String, Boolean> groupUIDs = new HashMap<String, Boolean>(); // Groups are collections of members.
    private HashMap<String, Boolean> skills = new HashMap<>(); //skills required for class

    private String id;// id is used for the school (ie, the CRN, or BIO3432, etc)

    @Exclude
    public DatabaseReference dbr = null;

    public Class() {
        super();
    }

//    public Class(@NonNull String name,
//                 @NonNull String id,
//                 @NonNull Professor professor // Professor is NEVER null, because a class can't be taught without a professor
//    ) {
//        this.name = name;
//        this.id = id;
//        this.professorUID = professor.UID;
//        updateRTDB();
//    }

    public Class(@NonNull String name,
                 @NonNull String id,
                 @NonNull String professorUID // Professor is NEVER null, because a class can't be taught without a professor
    ) {
        this.name = name;
        this.id = id;
        this.professorUID = professorUID;
        updateRTDB();
    }

    // Dummy class for Adding classes
    public Class(String name) {
        this.name = name;
        this.id = "";
    }

    @Exclude
    private void updateRTDB() {
        // TODO: Fix multiple database versions of the same class

        if (UID != null) {
            dbr = db.get().child(this.loc()).child(UID);
            dbr.setValue(this);
        } else if (dbr == null) {
            dbr = db.get().child(this.loc()).push();
            this.UID = dbr.getKey();
            dbr.setValue(this);
        } else {
            dbr.setValue(this);
        }
    }

    public String getProfessorUID() {
        return professorUID;
    }


    //TODO: I had to comment out UpdateRTDB as extra classes were being made due to unknown UID
    public void setProfessorUID(String professorUID) {
        this.professorUID = professorUID;
        //updateRTDB();
    }

    public HashMap<String, Boolean> getMemberUIDs() {
        return memberUIDs;
    }

    public void setMemberUIDs(HashMap<String, Boolean> memberUIDs) {
        this.memberUIDs = memberUIDs;
        //updateRTDB();
    }

    public HashMap<String, Boolean> getGroupUIDs() {
        return groupUIDs;
    }

    public void setGroupUIDs(HashMap<String, Boolean> groupUIDs) {
        this.groupUIDs = groupUIDs;
        //updateRTDB();
    }

    public HashMap<String, Boolean> getSkills() {
        return skills;
    }

    public void setSkills(HashMap<String, Boolean> skills) {
        this.skills = skills;
        //updateRTDB();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        //updateRTDB();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        //updateRTDB();
    }

    /**
     * NOTE : THIS PERFORMS YOUR ValueEventListener ON EACH MEMBER OF THE CLASS
     * THIS IS INTENDED TO BE USED WHEN POPULATING
     */
    public void getMembersAsync(ValueEventListener valueEventListener) {
        for (String c : memberUIDs.keySet()) {
            dbr.child(this.loc()).child(c).addListenerForSingleValueEvent(valueEventListener);
        }
    }

    public Member addStudent(Student student) {
        return new Member(student, this);
    }

    public void addMembers(Collection<? extends Member> members) {
        for (Member m : members) {
            this.memberUIDs.put(m.getUID(), true);
        }
        updateRTDB();
    }

    public void addMember(Member member) {
        this.memberUIDs.put(member.getUID(), true);
        updateRTDB();
    }

    public void removeMember(Member member) {
        this.memberUIDs.remove(member.getUID());
        updateRTDB();
    }

    /**
     * NOTE : THIS PERFORMS YOUR ValueEventListener ON EACH CLASS THE STUDENT IS TAKING
     * THIS IS INTENDED TO BE USED WHEN POPULATING
     */
    public void getSkillsAsync(ValueEventListener valueEventListener) {
        dbr.child("skills").addListenerForSingleValueEvent(valueEventListener);
    }

    /*public void addSkills(Collection<? extends Skills> skills) {
        for (Skills s : skills) {
            this.addSkill(s);
        }

    }*/

    public void addSkill(String s) {
        this.skills.put(s, true);
        updateRTDB();
    }

    public void removeSkill(String s) {
        this.skills.remove(s);
        updateRTDB();
    }

    @Exclude
    @Override
    public final String loc() {
        return "Classes";
    }
}
