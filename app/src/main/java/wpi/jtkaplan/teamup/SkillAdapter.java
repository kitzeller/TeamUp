package wpi.jtkaplan.teamup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import wpi.jtkaplan.teamup.model.Skills;

public class SkillAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> skills;

    public SkillAdapter(Context context, ArrayList<String> skills) {
        this.context = context;
        this.skills = skills;
    }

    @Override
    public int getCount() {
        return skills.size();
    }

    @Override
    public String getItem(int position) {
        return skills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        //inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.skill_rating, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //get current item to be displayed
        final String currentSkill = getItem(position);

        //sets the text for item name
        viewHolder.skillName.setText(currentSkill);

        //idLookup.put(convertView.findViewById(R.id.one, 1)
        viewHolder.levelChoices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioID = group.getCheckedRadioButtonId();
                int level = -1;
                /*
                switch(radioID){
                    case R.id.one:
                        level = 1; break;
                    case R.id.two:
                        level = 2; break;
                    case R.id.three:
                        level = 3; break;
                    case R.id.four:
                        level = 4; break;
                    case R.id.five:
                        level = 5; break;
                }*/
                level = Integer.valueOf(((RadioButton) (viewHolder.view.findViewById(radioID))).getText().toString());
                //TODO call addSkill function on user to set skill with skillLevel
                System.out.println("I'm clicked " + level);
                Skills skillsObj = UserPreferences.getSkillsObj();
                skillsObj.addSkill(currentSkill, level);
                }
            });

        //returns the view for the current row
        return convertView;
    }

    private class ViewHolder {
        TextView skillName;
        RadioGroup levelChoices;
        View view;

        public ViewHolder(View view) {
            this.view = view;
            skillName = view.findViewById(R.id.txtSkills);
            levelChoices = view.findViewById(R.id.choices);
        }
    }

}
