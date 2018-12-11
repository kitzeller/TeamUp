package wpi.jtkaplan.teamup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wpi.jtkaplan.teamup.model.Class;
import wpi.jtkaplan.teamup.model.Group;
import wpi.jtkaplan.teamup.model.Member;
import wpi.jtkaplan.teamup.model.Student;
import wpi.jtkaplan.teamup.model.User;

public class ClassGroupsRecyclerViewFragment extends Fragment {

    private List<Group> groups;
    private HashMap<String, ArrayList<String>> groupUsers;
    private RecyclerView rv;
    private ClassGroupsRVAdapter adapter;
    private TextView txtNoGroup;
    private Button btnCreate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        rv = view.findViewById(R.id.rv);
        txtNoGroup = view.findViewById(R.id.txtNoGroup);
        btnCreate = view.findViewById(R.id.btnCreateGroups);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        return view;
    }

    private void initializeData() {
        groups = new ArrayList<>();
        groupUsers = new HashMap<>();
        Class selectedClass = UserPreferences.getSelectedClass();

        selectedClass.getGroupsListAsync(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Boolean> groupList = (HashMap<String, Boolean>) dataSnapshot.getValue();
                if (groupList != null) {
                    for (String groupID : groupList.keySet()) {
                        // For each group in a class
                        Group.getGroupAsync(groupID, new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Group g = dataSnapshot.getValue(Group.class);
                                if (g != null) {
                                    System.out.println("Group " + g.getName());
                                    groups.add(g);
                                    Group.getGroupMembersListAsync(g.UID, new ValueEventListener() {
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
                                                            if (studentObj != null) {
                                                                ArrayList<String> names;
                                                                if (groupUsers.get(g.UID) != null) {
                                                                    names = groupUsers.get(g.UID);
                                                                } else {
                                                                    names = new ArrayList<>();
                                                                }
                                                                names.add(studentObj.getName());
                                                                groupUsers.put(g.UID, names);
                                                                System.out.println("name: " + studentObj.getName());
                                                                adapter.notifyDataSetChanged();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                                adapter.notifyDataSetChanged();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    txtNoGroup.setText("No groups created yet.");
                    btnCreate.setVisibility(View.VISIBLE);
                    btnCreate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: Call Create Groups
                            Toast.makeText(getActivity(), "Creating Groups for " + selectedClass.getName() + "...",
                                    Toast.LENGTH_SHORT).show();

                            selectedClass.makeGroupsAsync();
                            initializeData();


                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initializeAdapter() {
        adapter = new ClassGroupsRVAdapter(groups, groupUsers);
        adapter.getItemId(groups.size() - 1);
        rv.setAdapter(adapter);
    }
}