package wpi.jtkaplan.teamup.model;

public class Professor extends User {

    public Professor(String name, String personality, String email) {
        super(name, personality, email);
    }

    public Professor(String name, String personality, String email, String bio) {
        super(name, personality, email, bio);
    }

    public Professor() {
        super();
    }

    @Override
    public final String loc() {
        return "Professors";
    }

}
