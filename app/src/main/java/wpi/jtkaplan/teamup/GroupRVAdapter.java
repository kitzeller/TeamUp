package wpi.jtkaplan.teamup;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import wpi.jtkaplan.teamup.image.PicassoCircleTransformation;
import wpi.jtkaplan.teamup.model.User;

public class GroupRVAdapter<UserType extends User> extends RecyclerView.Adapter<GroupRVAdapter.MemberViewHolder> {

    List<UserType> members;

    GroupRVAdapter(List<UserType> members) {
        this.members = members;
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder memberViewHolder, int i) {
        memberViewHolder.memberName.setText(members.get(i).getName());
        memberViewHolder.memberEmail.setText(members.get(i).getEmail());
        Picasso.get().load(members.get(i).getPhoto()).transform(new PicassoCircleTransformation())
                .into(memberViewHolder.memberPhoto);
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
        return members.size();
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
                            "You clicked member " + memberName.getText(),
                            Toast.LENGTH_SHORT);

                    toast.show();
                }
            });
        }
    }
}