package wpi.jtkaplan.teamup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import wpi.jtkaplan.teamup.model.Skill;

public class SkillAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Skill> skills;

    public SkillAdapter(Context context, ArrayList<Skill> skills) {
        this.context = context;
        this.skills = skills;
    }

    @Override
    public int getCount() {
        return skills.size();
    }

    @Override
    public Object getItem(int position) {
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
        final Skill currentSkill = (Skill) getItem(position);

        //sets the text for item name
        viewHolder.skillName.setText(currentSkill.getSkillName());

        //TODO get selected skill level from selected radio button using onClickListener and save to skill
        viewHolder.levelChoices.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int level = viewHolder.levelChoices.getCheckedRadioButtonId();
                    currentSkill.setSkillLevel(level);
                }
            });

        //returns the view for the current row
        return convertView;
    }

    private class ViewHolder {
        TextView skillName;
        RadioGroup levelChoices;

        public ViewHolder(View view) {
            skillName = (TextView) view.findViewById(R.id.txtSkills);
            levelChoices = (RadioGroup) view.findViewById(R.id.choices);
        }
    }

}
