package wpi.jtkaplan.teamup.model;

import android.support.annotation.NonNull;

/**
 * Class model
 */
public class Class extends DeclarativeElement {
    String name;
    Professor professor;
    Member[] members; // Members are students that belong to this class. This allows students to quickly/neatly index many skills for many classes
    String id;// id is used for the school (ie, the CRN, or BIO3432, etc)

    public Class(@NonNull String name,
                 @NonNull String id,
                 @NonNull Professor professor // Professor is NEVER null, because a class can't be taught without a professor
    ) {
        this.name = name;
        this.id = id;
        this.professor = professor;
    }

    @Deprecated // TODO : DELETE THIS CONSTRUCTOR
    public Class(@NonNull String name,
                 @NonNull String id,
                 @NonNull String professor // Professor is NEVER null, because a class can't be taught without a professor
    ) {
        this.name = name;
        this.id = id;
        this.professor = new Professor(professor, "null", "null");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfessor() {
        return professor.getEmail();
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;// TODO : Re-asses; will we ever call setProfessor? Once a class is made, the professor is fixed.
        // TODO : Also NOTE : Professors have a list of classes they are a part of, and changing the professor of a class will need to also change the classes of a professor.
    }

    public String loc() {
        return "Classes";
    }
}
