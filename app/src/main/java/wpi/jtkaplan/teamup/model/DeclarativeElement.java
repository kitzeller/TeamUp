package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

abstract class DeclarativeElement extends Element {
    // This is used for firebase

    public DeclarativeElement() {
    }

    public String UID;

    @Exclude
    @Override
    public void getAsync(String uid, ValueEventListener vel) {
        DatabaseReference child = db.get().child(this.loc()).child(uid);
        child.addListenerForSingleValueEvent(vel);
    }
}
