package wpi.jtkaplan.teamup.model;

/**
 * Member model
 */
public class Student extends User {

    public Student(String name, String personality, String email, String bio) {
        super(name, personality, email, bio);
    }

    public Student(String name, String personality, String email) {
        super(name, personality, email);
    }

    public Student() {
        super();
    }



    @Override
    public final String loc() {
        return "Students";
    }

}
