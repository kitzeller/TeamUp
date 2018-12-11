package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Member model
 */
public class Member extends RelationalElement<Student, Class> {

    /*A RelationalElement describes the RELATION between a
     *   User (param0)
     *   Object (param1)
     *wherein the user uses the object.
     * For our case, a member is a student which "uses" (read: is a member of) a class
     * */

    public String groupUID;

    // ID of other members I have rated to rating
    private HashMap<String, Integer> ratingsOutward;

    //TODO : Implement finding out whether this member is in a group or not
    //TODO : implement a rating system within the model

    @Exclude
    DatabaseReference dbr = null;

    // NOTE : MEMBER OBJECTS ARE CREATED BY CALLING "ADD STUDENT" FROM A CLASS
    Member() {
        super();
    }

    // TODO : implement
    public Member(Student s, Class c) {
        super(s, c);
        updateRTDB();
    }
    /*
    @Exclude
    private void updateRTDB() {
        if (dbr == null) {
            dbr = db.get().child(this.loc()).child(this.getUID());
            dbr.setValue(this);
        } else {
            dbr.setValue(this);
        }
    }*/

    @Override
    final String loc() {
        return "Members";
    }

    @Override
    public void getAsync(String uid, ValueEventListener valueEventListener) {
        DatabaseReference child = db.get().child(this.loc()).child(uid);
        child.addListenerForSingleValueEvent(valueEventListener);
    }

    public static void getAsyncMember(String uid, ValueEventListener valueEventListener) {
        DatabaseReference child = db.get().child("Members").child(uid);
        child.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getAsync(ValueEventListener valueEventListener) {
        DatabaseReference child = db.get().child(this.loc()).child(this.getUID());
        child.addListenerForSingleValueEvent(valueEventListener);
    }

    public void getSkillsAsync(ValueEventListener valueEventListener) {
        DatabaseReference child = db.get().child(new Skills().loc()).child(this.getUID());
        child.addListenerForSingleValueEvent(valueEventListener);
    }

    public void addMemberRating(String uid, int value){
        this.ratingsOutward.put(uid, value);
        updateRTDB();
    }


}
