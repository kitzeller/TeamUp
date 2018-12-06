package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.DatabaseReference;

/**
 * Students and Professors both inherit from this.
 */
public abstract class User extends DeclarativeElement {
    private String name;
    private String age;
    private String email;
    private String bio; // TODO bio related stuff
    private DatabaseReference dbr = null;

    public User(String name, String age, String email, String bio) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.bio = bio;
        updateRTDB();
    }

    public User(String name, String age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
        updateRTDB();
    }

    public User() {
    }

    private void updateRTDB() {
        if (dbr == null) {
            dbr = db.get().child(this.loc()).push();
            this.UID = dbr.getKey();
            dbr.setValue(this);
        } else {
            dbr.setValue(this);
        }
    }

    /*
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateRTDB();
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
        updateRTDB();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        updateRTDB();
    }
}
