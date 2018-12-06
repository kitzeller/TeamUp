package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

abstract class DeclarativeElement implements Element {
    // This is used for firebase

    public DeclarativeElement() {
    }

    public String UID;

    @Override
    public void get(String uid, ValueEventListener vel) {
        DatabaseReference child = db.get().child(this.loc()).child(uid);
        child.addListenerForSingleValueEvent(vel);
    }
}
