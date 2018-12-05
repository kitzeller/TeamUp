package wpi.jtkaplan.teamup.model;

/**
 * Member model
 */
public class Student extends User {

    public Student(String name, String age, String email, String bio) {
        super(name, age, email, bio);
    }

    public Student(String name, String age, String email) {
        super(name, age, email);
    }
}
