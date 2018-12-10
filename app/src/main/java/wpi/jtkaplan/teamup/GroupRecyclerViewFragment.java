package wpi.jtkaplan.teamup;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wpi.jtkaplan.teamup.model.Class;
import wpi.jtkaplan.teamup.model.Group;
import wpi.jtkaplan.teamup.model.Member;
import wpi.jtkaplan.teamup.model.Professor;
import wpi.jtkaplan.teamup.model.Student;
import wpi.jtkaplan.teamup.model.User;

public class GroupRecyclerViewFragment
        <UserType extends User> // A group view may want to include a professor as well as the students. likewise, we may have different types of users.
        extends Fragment { // TODO : use members instead of students??

    private List<User> members;
    private HashMap<String,String> usertoMember;

    private RecyclerView rv;
    private GroupRVAdapter adapter;
    private TextView txtNoGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        rv = view.findViewById(R.id.rv);
        txtNoGroup = view.findViewById(R.id.txtNoGroup);
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
        usertoMember = new HashMap<>();
        // if professor get all students
        Class selected = UserPreferences.getSelectedClass();
        String O_UID = selected.UID;
        User student = UserPreferences.getCurrentUser();
        String S_UID = student.UID;

        String memberUID = S_UID + ":" + O_UID;

        Member.getAsyncMember(memberUID, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member m = dataSnapshot.getValue(Member.class);
                if (m.groupID != null){
                    // TODO: Get group members
                    Group.getGroupMembersListAsync(m.groupID, new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap<String, Boolean> memberMap = (HashMap<String, Boolean>) dataSnapshot.getValue();
                            if (memberMap != null) {
                                for (String key : memberMap.keySet()) {
                                    String[] values = key.split("\\:");
                                    String studentUID = values[0];
                                    String classUID = values[1];
                                    System.out.println("SUID " + studentUID);

                                    User.getUserFromPref(studentUID, "Students", new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Student studentObj = dataSnapshot.getValue(Student.class);
                                            members.add(studentObj);
                                            usertoMember.put(studentUID,key);
                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    txtNoGroup.setText("Group has not been created.");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initializeAdapter() {
        adapter = new GroupRVAdapter(members,usertoMember);
        adapter.getItemId(members.size() - 1);
        rv.setAdapter(adapter);
    }
}