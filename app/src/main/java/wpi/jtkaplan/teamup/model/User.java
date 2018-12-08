package wpi.jtkaplan.teamup.model;

import android.support.annotation.Nullable;

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
    private String personality;
    private String email;
    private String bio; // TODO bio related stuff
    private HashMap<String, Boolean> classUIDs = new HashMap<String, Boolean>();

    private int numClasses = -1;
    private int numGroups = -1;

    @Exclude
    public DatabaseReference dbr = null;

    public User(String name, @Nullable String personality, String email, String bio) {
        this.name = name;
        if (personality == null) {
            this.personality = "";
        } else {
            this.personality = personality;
        }
        this.email = email;
        this.bio = bio;
        updateRTDB();
    }

    public User(String name, @Nullable String personality, String email) {
        this.name = name;
        if (personality == null) {
            this.personality = "";
        } else {
            this.personality = personality;
        }
        this.email = email;
        updateRTDB();
    }

    public User() {
        super();
    }

    @Exclude
    private void updateRTDB() {
        if( UID != null){
            dbr = db.get().child(this.loc()).child(UID);
            dbr.setValue(this);
        }
        else if (dbr == null) {
            dbr = db.get().child(this.loc()).push();
            this.UID = dbr.getKey();
            dbr.setValue(this);
        } else {
            dbr.setValue(this);
        }
    }

    public void addUIDEmailRef() {
        String[] emailSplit = this.email.split("\\.");
        String email = String.join("", emailSplit);
        System.out.println("Email " + email);
        DatabaseReference database = db.get().child("E2U").child(email);
        database.setValue(this.UID + ":::" + this.loc());
    }

    public static void getUIDEmailRef(String email, ValueEventListener valueEventListener) {
        String[] emailSplit = email.split("\\.");
        String emailVal = String.join("", emailSplit);
        DatabaseReference database = db.get().child("E2U").child(emailVal);
        database.addListenerForSingleValueEvent(valueEventListener);
    }

    public static void getUserFromPref(String uid, String loc, ValueEventListener valueEventListener) {
        DatabaseReference database = db.get().child(loc).child(uid);
        database.addListenerForSingleValueEvent(valueEventListener);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateRTDB();
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
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
        this.numClasses++;
        this.classUIDs.put(c.UID, true);
        updateRTDB();
    }

    public void removeClass(Class c) {
        this.numClasses--;
        this.classUIDs.remove(c.UID);
        updateRTDB();
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
        updateRTDB();
    }

    public int getNumClasses() {
        return numClasses;
    }

    public void setNumClasses(int numClasses) {
        this.numClasses = numClasses;
        updateRTDB();
    }

    public int getNumGroups() {
        return numGroups;
    }

    public void setNumGroups(int numGroups) {
        this.numGroups = numGroups;
        updateRTDB();
    }
}
