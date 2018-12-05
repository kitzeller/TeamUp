package wpi.jtkaplan.teamup;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import wpi.jtkaplan.teamup.model.Member;

public class MemberFragment extends Fragment {

    ImageView profileImage;
    private StorageReference mStorageRef;
    private FirebaseUser mUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_details, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser(); // TODO : change this to get the requested (not necessarily current) user
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Member user = new Member(mUser.getDisplayName(), "18", mUser.getEmail());

        ((TextView) v.findViewById(R.id.account_email)).setText(user.getEmail());

        if (user.getName() != null) {
            ((TextView) v.findViewById(R.id.account_name)).setText(user.getName());
        }

        profileImage = v.findViewById(R.id.account_picture);
        Uri photoUrl = mUser.getPhotoUrl();
        String uid = mUser.getUid();

        // Using Picasso API to load image from URL into ImageView
        if (photoUrl != null) {
            Picasso.get().load(photoUrl).into(profileImage);
        }

        return v;
    }

}