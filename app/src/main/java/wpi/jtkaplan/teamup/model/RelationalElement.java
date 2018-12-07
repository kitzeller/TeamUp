package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

abstract class RelationalElement
        <UserOfRelation extends DeclarativeElement,
                ObjectOfRelation extends DeclarativeElement> extends Element {

    public UserOfRelation user;
    public ObjectOfRelation object;

    private static final String sep = ":"; // the seperator for joining UIDs of the user/object

    public RelationalElement() {
    }

    protected RelationalElement(UserOfRelation user, ObjectOfRelation object) {
        this.user = user;
        this.object = object;
    }

    @Exclude
    public final static String getUID(String userUID, String objectUID) {
        return userUID + sep + objectUID;
    }

    @Exclude
    public abstract void getAsync(ValueEventListener valueEventListener); // Used to get existing Elements from the database

    @Exclude
    public final String getUID() {
        return user.UID + sep + object.UID;
    }

    /*
    @Override
    public void getAsync(String uid, ValueEventListener vel) {
        DatabaseReference child = db.get().child(loc).child(uid);
        child.addListenerForSingleValueEvent(vel);
    }*/

    /*
    public void get(ValueEventListener vel) {
        DatabaseReference child = db.get().child(this.loc()).child(this.getUID());
        child.addListenerForSingleValueEvent(vel);
    }
    */
}
