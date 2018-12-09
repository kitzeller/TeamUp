package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

abstract class DeclarativeElement extends Element {
    // This is used for firebase

    public DeclarativeElement() {
        super();
    }

    @Exclude
    public DatabaseReference dbr = null;

    @Exclude
    void updateRTDB() {
        // TODO: Fix multiple database versions of the same class

        if (dbr == null && UID != null) { // if we have a UID, but no reference in the db
            dbr = db.get().child(this.loc()).child(UID); // get the db reference by UID
            dbr.keepSynced(true);
        } else if (dbr == null && UID == null) { // if we have no uid, and no reference in the db
            dbr = db.get().child(this.loc()).push();// make a new reference and set the UID accordingly
            this.UID = dbr.getKey();
            dbr.keepSynced(true);
        }
        if (UID == null && dbr != null) {
            System.out.print("ERROR IN UPDATERTDB:: ENCOUNTERED A DBR WITHOUT A UID");
        }
        dbr.setValue(this);
    }

    public String UID;

    //@Exclude
    @Override
    public void getAsync(String uid, ValueEventListener vel) {
        DatabaseReference child = db.get().child(this.loc()).child(uid);
        child.addListenerForSingleValueEvent(vel);
    }
}
