package wpi.jtkaplan.teamup;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wpi.jtkaplan.teamup.model.Group;
import wpi.jtkaplan.teamup.model.Student;
import wpi.jtkaplan.teamup.model.User;

public class ClassGroupsRVAdapter extends RecyclerView.Adapter<ClassGroupsRVAdapter.MemberViewHolder> {

    private List<Group> groups;
    private HashMap<String, ArrayList<String>> groupUsers;


    ClassGroupsRVAdapter(List<Group> groups, HashMap<String, ArrayList<String>> groupUsers) {
        this.groups = groups;
        this.groupUsers = groupUsers;
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder memberViewHolder, int i) {
        memberViewHolder.groupName.setText(groups.get(i).getName());
        System.out.println();

        if (groupUsers.get(groups.get(i).UID) != null){
            memberViewHolder.groupMembers.setText(groupUsers.get(groups.get(i).UID).toString());
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_group_details, viewGroup, false);
        MemberViewHolder cvh = new MemberViewHolder(v);
        return cvh;
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {

        View memberView;
        TextView groupName;
        TextView groupMembers;

        MemberViewHolder(View itemView) {
            super(itemView);
            memberView = itemView.findViewById(R.id.cv_group);
            groupName = itemView.findViewById(R.id.group_name);
            groupMembers = itemView.findViewById(R.id.group_members);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(view.getContext(),
                            "You clicked group " + groupName.getText(),
                            Toast.LENGTH_SHORT);

                    toast.show();
                }
            });
        }
    }
}