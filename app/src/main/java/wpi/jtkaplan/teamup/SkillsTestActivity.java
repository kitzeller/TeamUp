package wpi.jtkaplan.teamup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


import wpi.jtkaplan.teamup.model.Member;
import wpi.jtkaplan.teamup.model.Skill;
import wpi.jtkaplan.teamup.model.Skills;
import wpi.jtkaplan.teamup.model.Student;

public class SkillsTestActivity extends AppCompatActivity {

    private ArrayList<String> skillsList = new ArrayList<>();
    private Skills skills;
    private SkillAdapter adapter;

    private Student user;
    private wpi.jtkaplan.teamup.model.Class lecture;
    private Member member;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills_test);

        user = new Student("Bob", "18", "email@email.com");
        lecture = new wpi.jtkaplan.teamup.model.Class("science", "1234", "tian");
        member = new Member(user, lecture);
        skills = new Skills(user, lecture);

        lecture.addSkill("Python");
        lecture.addSkill("Java");


        lecture.getSkillsAsync(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                skills = dataSnapshot.getValue(Skills.class); //TODO: returning nothing

                for (String s: skills.skills.keySet()){
                    skillsList.add(s);
                }

                adapter = new SkillAdapter(getBaseContext(), skillsList);
                ListView lvSkills = (ListView) findViewById(R.id.question_list);
                lvSkills.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //error
            }
        });


    }




}





