package wpi.jtkaplan.teamup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

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
        final String currentSkill = (String) getItem(position);

        //sets the text for item name
        viewHolder.skillName.setText(currentSkill);

        viewHolder.levelChoices.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int level = viewHolder.levelChoices.getCheckedRadioButtonId();
                    //TODO call addSkill function on user to set skill with skillLevel
                    //[user].addSkill(currentSkill, level);
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
