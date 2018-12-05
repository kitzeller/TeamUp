package wpi.jtkaplan.teamup.model;

/**
 * Students and Professors both inherit from this.
 */
public abstract class User extends DeclarativeElement {
    public String name;
    public String age;
    public String email;
    public String bio; // TODO bio related stuff

    public User(String name, String age, String email, String bio) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.bio = bio;
    }

    public User(String name, String age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
