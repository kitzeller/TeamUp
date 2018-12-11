package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Group model
 */
public class Group extends DeclarativeElement {
    String name;
    String classUID;
    HashMap<String, Boolean> members;

    public Group() {
        super();
    }

    public Group(String name, String classUID, HashMap<String, Boolean> members) {
        this.name = name;
        this.classUID = classUID;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    @Exclude
    public void setName(String name) {
        this.name = name;
    }

    public String getClassUID() {
        return classUID;
    }

    @Exclude
    public void setClassUID(String classUID) {
        this.classUID = classUID;
    }

    public HashMap<String, Boolean> getMembers() {
        return members;
    }

    @Exclude
    public void setMembers(HashMap<String, Boolean> members) {
        this.members = members;
    }

    public static void getGroupAsync(String groupID, ValueEventListener valueEventListener) {
        DatabaseReference database = db.get().child(new Group().loc());
        database.child(groupID).addListenerForSingleValueEvent(valueEventListener);
    }


    public void getGroupMembersAsync(ValueEventListener valueEventListener) {
        DatabaseReference database = db.get().child(new Group().loc());
        for (String m: this.members.keySet()) {
            database.child(UID).child(m).addListenerForSingleValueEvent(valueEventListener);
        }
    }

    public static void getGroupMembersListAsync(String uid, ValueEventListener valueEventListener) {
        DatabaseReference database = db.get().child(new Group().loc());
        database.child(uid).child("members").addListenerForSingleValueEvent(valueEventListener);
    }



    public String loc() {
        return "Groups";
    }
}
