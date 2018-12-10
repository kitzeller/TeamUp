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
import android.view.Menu;
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

public class ProfileFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    String mCurrentPhotoPath;

    private ImageView profileImage;
    private StorageReference mStorageRef;
    private FirebaseUser mUser;
    private String m_Text = "";
    private EditText textBio;
    private TextView textPersonality;
    private TextView textGroups;
    private TextView textClasses;

    private String loc;
    private String uid;
    User user = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        profileImage = v.findViewById(R.id.profile_account_picture);
        textBio = v.findViewById(R.id.PROFILE_bio);
        textClasses = v.findViewById(R.id.PROFILE_classTxt);
        textGroups = v.findViewById(R.id.PROFILE_groupTxt);
        textPersonality = v.findViewById(R.id.PROFILE_perTxt);

        uid = UserPreferences.read(UserPreferences.UID_VALUE,null);
        loc = UserPreferences.read(UserPreferences.LOC_VALUE,null);
        System.out.println("Loading " + uid + " " + loc);

        if (loc.equals("Professors")){
            ((TextView) v.findViewById(R.id.PROFILE_perLabel)).setText("");
            ((TextView) v.findViewById(R.id.PROFILE_groupLabel)).setText("");
            ((TextView) v.findViewById(R.id.PROFILE_groupTxt)).setText("");
            ((TextView) v.findViewById(R.id.PROFILE_perTxt)).setText("");
        }

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


        Button signOut = v.findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        final Button btnEdit = v.findViewById(R.id.PROFILE_btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), btnEdit);
                //Inflating the Popup using xml file
                popup.getMenu().add(Menu.NONE, 1, 1, "Edit Bio");
                if (loc.equals(UserPreferences.STUDENT)) {
                    popup.getMenu().add(Menu.NONE, 2, 2, "Edit Myers Briggs");
                    popup.getMenu().add(Menu.NONE, 3, 3, "Open Myers Briggs Test");
                }

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == 1) {
                            textBio.setEnabled(true);
                            textBio.requestFocus();
                        } else if (item.getItemId() == 2) {
                            editMyersBriggs();
                        } else if (item.getItemId() == 3){
                            openMyersBriggs();
                        }
                        Toast.makeText(
                                getActivity(),
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method


        return v;
    }

    private void loginSetup(View v) {
        ((TextView) v.findViewById(R.id.profile_account_email)).setText(user.getEmail());

        if (user.getName() != null) {
            ((TextView) v.findViewById(R.id.profile_account_name)).setText(user.getName());
        }

        Uri photoUrl = mUser.getPhotoUrl();
        String uid = mUser.getUid();
        textBio.setText(user.getBio());
        textBio.setEnabled(false);

        if (loc.equals(new Student().loc())) {
            textPersonality.setText(user.getPersonality());
            textClasses.setText(Integer.toString(user.getNumClasses()));
            textGroups.setText(Integer.toString(user.getNumGroups()));
        } else if (loc.equals(new Professor().loc())) {
            textClasses.setText(Integer.toString(user.getNumClasses()));
        }

        // Using Picasso API to load image from URL into ImageView
        if (photoUrl != null) {
            Picasso.get().load(photoUrl).transform(new PicassoCircleTransformation())
                    .into(profileImage);
        }

        // Request for Camera permission at runtime
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)
                    getContext(), Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions((Activity) getContext(),
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = File.createTempFile("profile", ".jpg", dir);
                        mCurrentPhotoPath = photoFile.getAbsolutePath();
                    } catch (IOException ex) {

                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getActivity(), "wpi.jtkaplan.teamup.fileprovider", photoFile);
                        System.out.println("puri" + photoURI);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);


                    }
                }
            }
        });

        textBio.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    textBio.setEnabled(false);
                    user.setBio(textBio.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                uploadImage(mCurrentPhotoPath);
            }
        }
    }

    private void uploadImage(String path) {
        Uri file = Uri.fromFile(new File(path));

        // Store photo in Google Cloud as {email}/profile.jpg
        StorageReference usersRef = mStorageRef.child(mUser.getEmail() + "/profile.jpg");

        usersRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        // Set user PhotoUri to new uploaded photo
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(downloadUrl)
                                .build();

                        user.setPhoto(downloadUrl.toString());

                        mUser.updateProfile(profileUpdates);
                        Picasso.get().load(downloadUrl).transform(new PicassoCircleTransformation())
                                .into(profileImage);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });


    }

    void openMyersBriggs() {
        String url = "https://www.16personalities.com/free-personality-test";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    void editMyersBriggs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Input Myer's Briggs Result");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                user.setPersonality(m_Text);
                textPersonality.setText(m_Text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


}