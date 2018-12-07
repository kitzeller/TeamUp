package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.HashMap;

/**
 * Students and Professors both inherit from this.
 */
public abstract class User extends DeclarativeElement {
    private String name;
    private String age;
    private String email;
    private String bio; // TODO bio related stuff
    private HashMap<String, Boolean> classUIDs = new HashMap<String, Boolean>();

    @Exclude
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

    /**
     * NOTE : THIS PERFORMS YOUR ValueEventListener ON EACH CLASS THE STUDENT IS TAKING
     * THIS IS INTENDED TO BE USED WHEN POPULATING
     */
    public void getClassesAsync(ValueEventListener valueEventListener) {
        for (String c : classUIDs.keySet()) {
            dbr.child(new Class().loc()).child(c).addListenerForSingleValueEvent(valueEventListener);
        }
    }

    public void addClasses(Collection<? extends Class> classes) {
        for (Class c : classes) {
            this.addClass(c);
        }

    }

    public void addClass(Class c) {
        if (this instanceof Student) {
            c.addStudent((Student) this);
        }
        this.classUIDs.put(c.UID, true);
        updateRTDB();
    }

    public void removeClass(Class c) {
        this.classUIDs.remove(c.UID);
        updateRTDB();
    }
}
