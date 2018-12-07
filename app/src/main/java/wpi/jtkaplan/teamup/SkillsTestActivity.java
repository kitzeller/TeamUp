package wpi.jtkaplan.teamup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import java.util.ArrayList;


import wpi.jtkaplan.teamup.model.Skill;

public class SkillsTestActivity extends AppCompatActivity {

    private ArrayList<Skill> skillsList = new ArrayList<>();
    private SkillAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills_test);

        //TODO: load skills required given classID in intent
        skillsList.add(new Skill("Python", 0));
        skillsList.add(new Skill("Java", 0));

        adapter = new SkillAdapter(this, skillsList);

        ListView lvSkills = (ListView) findViewById(R.id.question_list);
        lvSkills.setAdapter(adapter);

    }




}





