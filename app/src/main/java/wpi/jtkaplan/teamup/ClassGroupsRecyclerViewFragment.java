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
import wpi.jtkaplan.teamup.model.User;

public class ClassGroupsRecyclerViewFragment extends Fragment {

    private List<Group> groups;
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
                                System.out.println("Group " + g.getName());
                                groups.add(g);
                                adapter.notifyDataSetChanged();
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
                            Toast.makeText(getActivity(), "Creating Groups...",
                                    Toast.LENGTH_SHORT).show();
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
        adapter = new ClassGroupsRVAdapter(groups);
        adapter.getItemId(groups.size() - 1);
        rv.setAdapter(adapter);
    }
}