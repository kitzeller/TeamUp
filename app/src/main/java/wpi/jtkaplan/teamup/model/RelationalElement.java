package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

abstract class RelationalElement
        <UserOfRelation extends DeclarativeElement,
                ObjectOfRelation extends DeclarativeElement> extends Element {

    @Exclude
    private static final String sep = ":"; // the separator for joining UIDs of the user/object
    protected String S_UID;
    protected String O_UID;
    @Exclude
    UserOfRelation user;
    @Exclude
    ObjectOfRelation object;

    public RelationalElement() {
    }

    protected RelationalElement(UserOfRelation user, ObjectOfRelation object) {
        this.user = user;
        S_UID = this.user.UID;

        this.object = object;
        O_UID = this.object.UID;
    }

    @Exclude
    public final static String getUID(String userUID, String objectUID) {
        return userUID + sep + objectUID;
    }

    @Exclude
    public DatabaseReference dbr = null;

    @Exclude
    void updateRTDB() {
        // TODO: Fix multiple database versions of the same class
        String UID = this.getUID();
        if (dbr == null && UID != null) { // if we have a UID, but no reference in the db
            dbr = db.get().child(this.loc()).child(UID); // get the db reference by UID
        } else if (dbr == null && UID == null) { // if we have no uid, and no reference in the db
            dbr = db.get().child(this.loc()).push();// make a new reference and set the UID accordingly
            UID = dbr.getKey();
        }
        if (UID == null && dbr != null) {
            System.out.print("ERROR IN UPDATERTDB:: ENCOUNTERED A DBR WITHOUT A UID");
        }
        dbr.setValue(this);
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
