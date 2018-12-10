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

import java.util.HashMap;
import java.util.List;

import wpi.jtkaplan.teamup.image.PicassoCircleTransformation;
import wpi.jtkaplan.teamup.model.Member;
import wpi.jtkaplan.teamup.model.User;

public class GroupRVAdapter extends RecyclerView.Adapter<GroupRVAdapter.MemberViewHolder> {

    List<User> members;

    // user UID to Member UID
    HashMap<String, String> usertoMember;

    GroupRVAdapter(List<User> members, HashMap<String, String> userToMember) {
        this.members = members;
        this.usertoMember = userToMember;
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder memberViewHolder, int i) {
        String userID = members.get(i).UID;
        memberViewHolder.memberID = usertoMember.get(userID);
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
        String memberID;


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
                            "You clicked group member " + memberName.getText(),
                            Toast.LENGTH_SHORT);

                    toast.show();
                    Dialog rankDialog = new Dialog(view.getContext());
                    rankDialog.setContentView(R.layout.rank_dialog);
                    rankDialog.setCancelable(true);
                    RatingBar ratingBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);
                    TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
                    text.setText(memberName.getText() + "'s Ranking");

                    Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                    updateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Member currentMember = UserPreferences.getMember();
                            //TODO: Update ranking - check below code works
                            currentMember.addMemberRating(memberID, (int) ratingBar.getRating());
                            rankDialog.dismiss();
                        }
                    });
                    rankDialog.show();
                }
            });
        }
    }
}