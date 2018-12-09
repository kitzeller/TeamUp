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
import wpi.jtkaplan.teamup.model.Professor;
import wpi.jtkaplan.teamup.model.Skill;
import wpi.jtkaplan.teamup.model.Skills;
import wpi.jtkaplan.teamup.model.Student;
import wpi.jtkaplan.teamup.model.User;

public class SkillsTestActivity extends AppCompatActivity {

    private ArrayList<String> skillsList = new ArrayList<>();
    //private Skills skills;
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
                if (loc.equals("Students")) {
                    user = dataSnapshot.getValue(Student.class);
                    user.getSingleClassAsync(classUID, new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            lecture = dataSnapshot.getValue(Class.class);
                            if (lecture == null){
                                return;
                            }
                            skillsTitle.setText(lecture.getName() + " Skills Test");

                            member = new Member(user,lecture);
                            // TODO: Find how Skills class will work
                            //skills = new Skills(user,lecture);
                            lecture.getSkillsAsync(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    HashMap<String,Boolean> map = (HashMap<String,Boolean>) dataSnapshot.getValue(); //TODO: returning nothing

                                    for (String s: map.keySet()){
                                        skillsList.add(s);
                                    }

                                    adapter = new SkillAdapter(getBaseContext(), skillsList);
                                    ListView lvSkills = (ListView) findViewById(R.id.question_list);
                                    lvSkills.setAdapter(adapter);


                                    // on Submit skills button
                                    // Add student to class
                                    submitSkills.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // on Submit test
                                            user.addClass(lecture);

                                            Toast.makeText(SkillsTestActivity.this, "Submitted Skills Test" ,
                                                    Toast.LENGTH_SHORT).show();
                                            finish();

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    //error
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





