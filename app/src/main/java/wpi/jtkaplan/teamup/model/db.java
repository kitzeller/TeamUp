package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class db {

    static db _this = null;

    private static DatabaseReference mDatabase;

    private db() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true); // enable local disk persistence
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference get() {
        if (mDatabase == null) {
            _this = new db();
        }
        return mDatabase;
    }

    public static void preload(String loc, String uid) {
        DatabaseReference referenceToMe = db.get().child(loc).child(uid);
        referenceToMe.keepSynced(true); // keep this synced/cached for faster loading
        referenceToMe.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user;
                if (loc.equals(new Student().loc())) {
                    user = dataSnapshot.getValue(Student.class);
                } else /*if(loc.equals(new Professor().loc()))*/ {
                    user = dataSnapshot.getValue(Professor.class);
                }

                for (String classUID : user.getClassUIDs().keySet()) {
                    DatabaseReference referenceToMyClass = db.get().child(new Class().loc()).child(classUID);
                    referenceToMyClass.keepSynced(true);// keep this synced/cached for faster loading.
                    //referenceToMyClass // TODO : CONSIDER GOING DEEPER AND CACHING MORE RELEVANT OBJECTS
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
