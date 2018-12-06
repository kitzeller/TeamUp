package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.ValueEventListener;

public interface Element {
    String loc(); // used to get the location in the database of a particular part of the model

    void get(String uid, ValueEventListener valueEventListener); // Used to get existing Elements from the database
}
