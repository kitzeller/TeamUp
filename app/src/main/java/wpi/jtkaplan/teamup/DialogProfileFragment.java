package wpi.jtkaplan.teamup;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import wpi.jtkaplan.teamup.image.PicassoCircleTransformation;
import wpi.jtkaplan.teamup.model.Professor;
import wpi.jtkaplan.teamup.model.Student;
import wpi.jtkaplan.teamup.model.User;

/**
 * Created by mac on 12/10/18.
 */

public class DialogProfileFragment extends DialogFragment {


    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    String mCurrentPhotoPath;

    private ImageView profileImage;
    private EditText textBio;
    User user = null;

    public static DialogProfileFragment newInstance(String uid, String loc) {
        DialogProfileFragment yourDialogFragment = new DialogProfileFragment();

//        //example of passing args
        Bundle args = new Bundle();
        args.putString("uid", uid);
        args.putString("loc", loc);
        yourDialogFragment.setArguments(args);

        return yourDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String uid = getArguments().getString("uid");
        String loc = getArguments().getString("loc");


        View v = inflater.inflate(R.layout.fragment_profile_non_editable, null);

        profileImage = v.findViewById(R.id.profile_account_picture_ne);
        textBio = v.findViewById(R.id.PROFILE_bio_ne);

        User.getUserFromPref(uid, loc, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (loc.equals("Professors")) {
                    user = dataSnapshot.getValue(Professor.class);
                } else {
                    user = dataSnapshot.getValue(Student.class);
                }
                user.UID = uid;
                user.dbr = wpi.jtkaplan.teamup.model.db.get().child(loc).child(uid);
                System.out.println("uid " + user.UID);
                loginSetup(v);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

    private void loginSetup(View v) {
        ((TextView) v.findViewById(R.id.profile_account_email_ne)).setText(user.getEmail());
        ((TextView) v.findViewById(R.id.profile_account_personality_ne)).setText(user.getPersonality());
        ((TextView) v.findViewById(R.id.profile_account_name_ne)).setText(user.getName());

        textBio.setText(user.getBio());
        textBio.setEnabled(false);


        // Using Picasso API to load image from URL into ImageView
        if (user.getPhoto() != null) {
            Picasso.get().load(user.getPhoto()).transform(new PicassoCircleTransformation())
                    .into(profileImage);
        }

    }
}