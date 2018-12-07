package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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

    public boolean isInGroup; // TODO : Implement finding out whether this member is in a group or not
    //TODO : implement a rating system within the model


    public Member() {
        super();
    }

    // TODO : implement
    public Member(Student s, Class c) {
        super(s, c);
    }


    @Override
    public final String loc() {
        return "Members";
    }

    @Override
    public void getAsync(String uid, ValueEventListener valueEventListener) {
        DatabaseReference child = db.get().child(this.getUID()).child(uid);
        child.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getAsync(ValueEventListener valueEventListener) {
        DatabaseReference child = db.get().child(this.getUID()).child(this.getUID());
        child.addListenerForSingleValueEvent(valueEventListener);
    }

    public void getSkillsAsync(ValueEventListener valueEventListener) {
        DatabaseReference child = db.get().child(new Skills().loc()).child(this.getUID());
        child.addListenerForSingleValueEvent(valueEventListener);
    }

}
