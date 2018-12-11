package wpi.jtkaplan.teamup;

import android.app.Activity;
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

    private String classID;
    private EditText idInput;

    private static final int REQUEST_CODE_ADD_CLASS = 101;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO : try to actually add this class in the backend.
        View v = inflater.inflate(R.layout.fragment_join_class, container, false);

        idInput = v.findViewById(R.id.JOIN_classidEditText);
        Button btn = v.findViewById(R.id.JOIN_addButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: send database reference of class
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Intent intent = new Intent(v.getContext(), SkillsTestActivity.class);
                // classID is the class UID
                classID = idInput.getText().toString();
                intent.putExtra("uid", classID);
                System.out.println("classID from frag " + classID);
                activity.startActivityForResult(intent, REQUEST_CODE_ADD_CLASS);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_ADD_CLASS == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                // Here
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ClassesRecyclerViewFragment()).addToBackStack(null).commit();

            } else {
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}