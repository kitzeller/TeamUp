package wpi.jtkaplan.teamup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import wpi.jtkaplan.teamup.model.Professor;
import wpi.jtkaplan.teamup.model.Student;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";
    protected String userType = "Students";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private EditText nameEditText = null;
    private EditText emailEditText = null;
    private RadioButton professorRadioButton = null;
    private RadioButton studentRadioButton = null;
    private EditText passwordEditText = null;
    private EditText bioEditText = null;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_account);
        Button create = findViewById(R.id.CA_createAccountButton);
        emailEditText = findViewById(R.id.CA_emailEditText);
        passwordEditText = findViewById(R.id.CA_passwordEditText);
        nameEditText = findViewById(R.id.CA_nameEditText);
        bioEditText = findViewById(R.id.CA_bioEditText);
        professorRadioButton = findViewById(R.id.CA_professorRadioButton);
        studentRadioButton = findViewById(R.id.CA_studentRadioButton);

        mAuth = FirebaseAuth.getInstance();

        professorRadioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                studentRadioButton.setChecked(false);
                professorRadioButton.setChecked(true);
                userType = "Professors";
            }
        });
        studentRadioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                professorRadioButton.setChecked(false);
                studentRadioButton.setChecked(true);
                userType = "Students";
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptCreateAccount();
            }
        });
    }

    // Set up the login form.
    private void updateUI() {
        // If success call MainActivity class
        Intent myIntent = new Intent(CreateAccountActivity.this, MainActivity.class);
        //myIntent.putExtra("email", currentUser.getEmail()); //Optional parameters
        CreateAccountActivity.this.startActivity(myIntent);
    }

    /*
    private void firebaseAuthLogin(String mEmail, String mPassword) {
        mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            currentUser = user;
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }*/

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptCreateAccount() {

        // Reset errors.
        emailEditText.setError(null);
        passwordEditText.setError(null);
        nameEditText.setError(null);
        bioEditText.setError(null);

        // Store values at the time of the login attempt.
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        final String name = nameEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }
        // Check for a valid name
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("This field is required");
            focusView = nameEditText;
            cancel = true;
        }
        // Check that the user entered a bio
        if (TextUtils.isEmpty(name)) {
            bioEditText.setError("Tell your teammates a bit about yourself!");
            focusView = bioEditText;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailEditText.setError(getString(R.string.error_invalid_email));
            focusView = emailEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Kick off a background task to perform the user register attempt.
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name).build();

                                user.updateProfile(profileUpdates);
                                currentUser = user;

                                if (userType.equals("Students")) {
                                    Student student = new Student(name, null, email, bioEditText.getText().toString());
                                    student.addUIDEmailRef();
                                    UserPreferences.write(UserPreferences.LOC_VALUE, userType);
                                    UserPreferences.write(UserPreferences.UID_VALUE, student.UID);
                                } else { //Professor
                                    // Shared Preferences
                                    Professor professor = new Professor(name, null, email, bioEditText.getText().toString());
                                    professor.addUIDEmailRef();
                                    UserPreferences.write(UserPreferences.LOC_VALUE, userType);
                                    UserPreferences.write(UserPreferences.UID_VALUE, professor.UID);
                                }

                                updateUI();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
