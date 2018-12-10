package wpi.jtkaplan.teamup;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import wpi.jtkaplan.teamup.image.PicassoCircleTransformation;
import wpi.jtkaplan.teamup.model.Group;
import wpi.jtkaplan.teamup.model.User;

public class ClassGroupsRVAdapter extends RecyclerView.Adapter<ClassGroupsRVAdapter.MemberViewHolder> {

    List<Group> groups;

    ClassGroupsRVAdapter(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder memberViewHolder, int i) {
        // TODO: Fix this for proper group stuff
        memberViewHolder.memberName.setText(groups.get(i).getName());
//        memberViewHolder.memberEmail.setText(members.get(i).getEmail());
//        Picasso.get().load(members.get(i).getPhoto()).transform(new PicassoCircleTransformation())
//                .into(memberViewHolder.memberPhoto);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_profile_details, viewGroup, false);
        MemberViewHolder cvh = new MemberViewHolder(v);
        return cvh;
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {

        View memberView;
        TextView memberName;
        TextView memberEmail;
        ImageView memberPhoto;


        MemberViewHolder(View itemView) {
            super(itemView);
            memberView = itemView.findViewById(R.id.cv_profile);

            memberName = itemView.findViewById(R.id.account_name);
            memberEmail = itemView.findViewById(R.id.account_email);
            memberPhoto = itemView.findViewById(R.id.account_picture);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(view.getContext(),
                            "You clicked group " + memberName.getText(),
                            Toast.LENGTH_SHORT);

                    toast.show();
                }
            });
        }
    }
}