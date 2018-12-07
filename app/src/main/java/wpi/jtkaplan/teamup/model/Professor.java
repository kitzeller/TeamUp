package wpi.jtkaplan.teamup.model;

public class Professor extends User {

    public Professor(String name, String age, String email) {
        super(name, age, email);
    }

    public Professor(String name, String age, String email, String bio) {
        super(name, age, email, bio);
    }

    public Professor() {
        super();
    }

    @Override
    public final String loc() {
        return "Professors";
    }
}
