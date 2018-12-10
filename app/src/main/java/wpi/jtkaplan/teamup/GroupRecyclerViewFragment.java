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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wpi.jtkaplan.teamup.model.Class;
import wpi.jtkaplan.teamup.model.Member;
import wpi.jtkaplan.teamup.model.Professor;
import wpi.jtkaplan.teamup.model.Student;
import wpi.jtkaplan.teamup.model.User;

public class GroupRecyclerViewFragment
        <UserType extends User> // A group view may want to include a professor as well as the students. likewise, we may have different types of users.
        extends Fragment { // TODO : use members instead of students??

    private List<User> members;
    private RecyclerView rv;

    private GroupRVAdapter adapter;

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
        // TODO: Need to get this data from Google Cloud for each member of the current group or class.

        members = new ArrayList<>();
        // if professor get all students
        Class selected = UserPreferences.getSelectedClass();
        System.out.println("Selected Class " + selected.getName());

        selected.getMembersListAsync(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Gets list of member uid, then split by ':' character and get student objects from ID
                System.out.println("List " + dataSnapshot.getValue());
                HashMap<String,Boolean> memberMap = (HashMap<String,Boolean>)dataSnapshot.getValue();
                for (String key: memberMap.keySet()){
                    String[] values = key.split("\\:");
                    String studentUID = values[0];
                    String classUID = values[1];
                    System.out.println("SUID " + studentUID);

                    User.getUserFromPref(studentUID, "Students", new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // TODO: Right now shows all students for Professor View and Student view - change for only professors and then groups for students
                            members.add(dataSnapshot.getValue(Student.class));
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        selected.getMembersAsync(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Member m = dataSnapshot.getValue(Member.class);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });





//        members.add(new Professor("DIFFERENT MODEL", "x", "x"));
//        members.add(new Student("Harvey Milk", "21", "im@straight.com"));
//        members.add(new Student("Frida Kahlo", "32", "nothurt@faithful.com"));
//        members.add(new Student("Edward Culkin", "8", "wheredid@gowrong.com"));
//        members.add(new Student("John Adams", "15", "emaildoesnt@exist.yet"));
//        members.add(new Student("Abraham Lincoln", "209", "dont@shootme.bro"));
    }

    private void initializeAdapter() {
        adapter = new GroupRVAdapter(members);
        adapter.getItemId(members.size() - 1);
        rv.setAdapter(adapter);
    }
}