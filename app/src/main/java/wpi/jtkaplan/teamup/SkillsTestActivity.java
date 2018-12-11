package wpi.jtkaplan.teamup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import wpi.jtkaplan.teamup.model.Class;
import wpi.jtkaplan.teamup.model.Member;
import wpi.jtkaplan.teamup.model.Skills;
import wpi.jtkaplan.teamup.model.Student;
import wpi.jtkaplan.teamup.model.User;

public class SkillsTestActivity extends AppCompatActivity {

    private ArrayList<String> skillsList = new ArrayList<>();
    private Skills skillsObj;
    private SkillAdapter adapter;
    private String classUID;

    private TextView skillsTitle;
    private Button submitSkills;

    private Student user;
    private wpi.jtkaplan.teamup.model.Class lecture;
    private Member member;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        classUID = intent.getStringExtra("uid");
        System.out.println("Class UID Intent " + classUID);

        setContentView(R.layout.activity_skills_test);

        skillsTitle = findViewById(R.id.skillsTestTitle);
        submitSkills = findViewById(R.id.btnSubmitSkills);

        String uid = UserPreferences.read(UserPreferences.UID_VALUE,null);
        String loc = UserPreferences.read(UserPreferences.LOC_VALUE,null);

        User.getUserFromPref(uid, loc, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (loc.equals(new Student().loc())) {
                    user = dataSnapshot.getValue(Student.class);
                    User.getSingleClassAsync(classUID, new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // TODO :: Check if dataSnapshot.getValue() is null -- if it is, then return early and tell the user that the class string they entered is invalid!!
                            //System.out.println("datasnap: " + dataSnapshot.getValue());

                            lecture = dataSnapshot.getValue(Class.class);
                            //System.out.println("Skills: " + lecture.getName());
                            System.out.println("NUMBER OF SKILLS CURRENTLY LOADED IN CLASS: " + Integer.toString(lecture.getSkills().keySet().size()));

                            skillsTitle.setText(lecture.getName() + " Skills Test");

                            // Need to set UID and dbr to retrieve skills
                            lecture.UID = classUID;
                            lecture.dbr = wpi.jtkaplan.teamup.model.db.get().child(lecture.loc()).child(classUID);

                            //member = new Member(user,lecture);

                            // TODO: Pass Skills object to list adapter - using User Pref Singleton?
                            skillsObj = new Skills(user,lecture);
                            UserPreferences.setSkillsObj(skillsObj);

                            HashMap<String, Boolean> map = lecture.getSkills();//(HashMap<String,Boolean>) dataSnapshot.getValue(); //TODO: returning nothing

                            for (String s : map.keySet()) {
                                skillsList.add(s);
                            }

                            adapter = new SkillAdapter(getBaseContext(), skillsList);
                            ListView lvSkills = findViewById(R.id.question_list);
                            lvSkills.setAdapter(adapter);


                            // on Submit skills button
                            // Add student to class
                            submitSkills.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // on Submit test
                                    user.addClass(lecture);

                                    Toast.makeText(SkillsTestActivity.this, "Submitted Skills Test",
                                            Toast.LENGTH_SHORT).show();
                                    Intent returnIntent = new Intent();
                                    setResult(RESULT_OK, returnIntent);
                                    finish();

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }



        });

    }




}





