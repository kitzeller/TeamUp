package wpi.jtkaplan.teamup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class AddClassFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO : try to actually add this class in the backend.
        View v = inflater.inflate(R.layout.fragment_join_class, container, false);

        EditText idInput = v.findViewById(R.id.JOIN_classidEditText);
        final String classID = idInput.getText().toString();
        Button btn = v.findViewById(R.id.JOIN_addButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: send database reference of class
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Intent intent = new Intent(v.getContext(), SkillsTestActivity.class);
                intent.putExtra("ClassID", classID);
                activity.startActivity(intent);

            }
        });



        return v;
    }
}