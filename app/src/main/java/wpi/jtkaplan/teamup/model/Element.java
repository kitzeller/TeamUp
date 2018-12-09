package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

abstract class Element {

    @Exclude
    public DatabaseReference dbr = null;

    @Exclude
    abstract void updateRTDB();


    @Exclude
    abstract String loc();// used to get the location in the database of a particular part of the model

    @Exclude
    public abstract void getAsync(String uid, ValueEventListener valueEventListener); // Used to get existing Elements from the database
}
