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
    String name;
    String professorUID;
    HashMap<String, Boolean> memberUIDs = new HashMap<String, Boolean>(); // Members are students that belong to this class. This allows students to quickly/neatly index many skills for many classes
    HashMap<String, Boolean> groupUIDs = new HashMap<String, Boolean>(); // Groups are collections of members.
    HashMap<String, Boolean> skills = new HashMap<>(); //skills required for class

    String id;// id is used for the school (ie, the CRN, or BIO3432, etc)

    @Exclude
    private DatabaseReference dbr = null;

    public Class() {
        super();
    }

    public Class(@NonNull String name,
                 @NonNull String id,
                 @NonNull Professor professor // Professor is NEVER null, because a class can't be taught without a professor
    ) {
        this.name = name;
        this.id = id;
        this.professorUID = professor.UID;
        updateRTDB();
    }


    @Deprecated // TODO : DELETE THIS CONSTRUCTOR  -- A CLASS MUST BE MADE BY A PROFESSOR
    public Class(@NonNull String name,
                 @NonNull String id,
                 @NonNull String professor // Professor is NEVER null, because a class can't be taught without a professor
    ) {
        this.name = name;
        this.id = id;
        this.professorUID = "-";//new Professor(professor, "null", "null");
        updateRTDB();
    }

    @Exclude
    private void updateRTDB() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateRTDB();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        updateRTDB();
    }

    public String getProfessor() {
        return professorUID;
    }

    public void setProfessor(Professor professor) {
        this.professorUID = professor.UID;// TODO : Re-assess; will we ever call setProfessor? Once a class is made, the professor is fixed.
        // TODO : Also NOTE : Professors have a list of classes they are a part of, and changing the professor of a class will need to also change the classes of a professor.
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
        dbr.child(new Skills().loc()).addListenerForSingleValueEvent(valueEventListener);
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
