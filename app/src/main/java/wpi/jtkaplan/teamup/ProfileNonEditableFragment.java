package wpi.jtkaplan.teamup;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import wpi.jtkaplan.teamup.image.PicassoCircleTransformation;
import wpi.jtkaplan.teamup.model.Professor;
import wpi.jtkaplan.teamup.model.Student;
import wpi.jtkaplan.teamup.model.User;

public class ProfileNonEditableFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    String mCurrentPhotoPath;

    private ImageView profileImage;
    private EditText textBio;
    User user = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_non_editable, container, false);

        profileImage = v.findViewById(R.id.profile_account_picture_ne);
        textBio = v.findViewById(R.id.PROFILE_bio_ne);

        String uid = UserPreferences.getSelectedClass().getProfessorUID();
        String loc = "Professors";

        User.getUserFromPref(uid, loc, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (loc.equals("Professors")){
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

        if (user.getName() != null) {
            ((TextView) v.findViewById(R.id.profile_account_name_ne)).setText(user.getName());
        }

        textBio.setText(user.getBio());
        textBio.setEnabled(false);


        // Using Picasso API to load image from URL into ImageView
        if (user.getPhoto() != null) {
            Picasso.get().load(user.getPhoto()).transform(new PicassoCircleTransformation())
                    .into(profileImage);
        }

    }

}