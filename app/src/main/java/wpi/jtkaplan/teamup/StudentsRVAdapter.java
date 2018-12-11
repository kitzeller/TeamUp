package wpi.jtkaplan.teamup;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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

public class StudentsRVAdapter<UserType extends User> extends RecyclerView.Adapter<StudentsRVAdapter.MemberViewHolder> {

    List<UserType> members;

    StudentsRVAdapter(List<UserType> members) {
        this.members = members;
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder memberViewHolder, int i) {
        memberViewHolder.memberName.setText(members.get(i).getName());
        memberViewHolder.memberEmail.setText(members.get(i).getEmail());
        memberViewHolder.member = members.get(i);

        if (members.get(i).getPhoto() != null) {
            Picasso.get().load(members.get(i).getPhoto()).transform(new PicassoCircleTransformation())
                    .into(memberViewHolder.memberPhoto);
        }
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

        User member;
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

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    ////GroupRecyclerViewFragment memberViewFragment = new GroupRecyclerViewFragment();
                    HomeFragment homeFrag = new HomeFragment();
                    //activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFrag).addToBackStack(null).commit();


                    DialogFragment newFragment = DialogProfileFragment.newInstance(member.UID, "Students");
                    newFragment.show(activity.getSupportFragmentManager(), "dialog");

                    Toast toast = Toast.makeText(view.getContext(),
                            "You clicked member " + memberName.getText(),
                            Toast.LENGTH_SHORT);

                    toast.show();
                }
            });
        }
    }
}