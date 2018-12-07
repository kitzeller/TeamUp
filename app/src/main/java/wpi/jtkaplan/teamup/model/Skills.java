package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Member model
 */
public class Skills extends RelationalElement<Student, Class> {

    /*A RelationalElement describes the RELATION between a
     *   User (param0)
     *   Object (param1)
     *wherein the user uses the object.
     * For our case, a Skill is a student's abilities for a class
     * */

    // TODO : implement a dictionary of {String skillname : int skillscore} to keep track of this user's skills in this class

    public Skills(Student s, Class c) {
        super(s, c);
    }

    public Skills() {
        super();
    }

    @Override
    public final String loc() {
        return "Skills";
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



}
