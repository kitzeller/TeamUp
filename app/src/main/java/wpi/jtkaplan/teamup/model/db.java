package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class db {

    static db _this = null;

    private static DatabaseReference mDatabase;

    private db() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference get() {
        if (mDatabase == null) {
            _this = new db();
        }
        return mDatabase;
    }
}
