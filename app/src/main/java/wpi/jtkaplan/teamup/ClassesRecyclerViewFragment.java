package wpi.jtkaplan.teamup;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import wpi.jtkaplan.teamup.model.Class;
import wpi.jtkaplan.teamup.model.Professor;
import wpi.jtkaplan.teamup.model.Student;
import wpi.jtkaplan.teamup.model.User;

public class ClassesRecyclerViewFragment extends Fragment {

    private List<Class> classes;
    private RecyclerView rv;
    private User user;
    private ClassesRVAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        rv = view.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        return view;
    }

    private void initializeData() {
        // TODO: Need to get this data from Google Cloud for each user.
        classes = new ArrayList<Class>();

        String uid = UserPreferences.read(UserPreferences.UID_VALUE, null);
        String loc = UserPreferences.read(UserPreferences.LOC_VALUE, null);

        User.getUserFromPref(uid, loc, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (loc.equals("Professors")) {
                    System.out.println("Professors get class");
                    user = dataSnapshot.getValue(Professor.class);
                    loadClasses();
                } else if (loc.equals("Students")){
                    user = dataSnapshot.getValue(Student.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



//        Class democlass = new Class("Intro to Psy", "PSY 1402", "Mr Wilson");
//        new Student("test", "test", "test").addClass(democlass);
//        classes.add(democlass);
//        classes.add(new Class("Intro to Computing", "CS 4518","Tian Guo"));
//        classes.add(new Class("Fun and Games", "FUN 101","Mr Jake"));
//        classes.add(new Class("Intro to Games", "IMGD 1001","Mr Baker"));
    }

    private void loadClasses(){
        user.getClassesAsync(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Adding class");
                wpi.jtkaplan.teamup.model.Class toAdd = dataSnapshot.getValue(wpi.jtkaplan.teamup.model.Class.class);
                adapter.addClass(toAdd);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void initializeAdapter() {
        adapter = new ClassesRVAdapter(classes);
        adapter.getItemId(classes.size() - 1);
        rv.setAdapter(adapter);
    }
}