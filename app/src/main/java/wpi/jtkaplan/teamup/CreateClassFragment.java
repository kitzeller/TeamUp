package wpi.jtkaplan.teamup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hootsuite.nachos.ChipConfiguration;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.ChipSpan;
import com.hootsuite.nachos.chip.ChipSpanChipCreator;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.hootsuite.nachos.tokenizer.SpanChipTokenizer;

import java.util.ArrayList;

import wpi.jtkaplan.teamup.model.Professor;
import wpi.jtkaplan.teamup.model.Student;
import wpi.jtkaplan.teamup.model.User;

/**
 * Class for creating a class
 */

public class CreateClassFragment extends Fragment { // TODO :: REFACTOR THE NAME FOR CLASSFRAGMENT -- THIS IS MISLEADING?

    private ArrayList<String> tags;

    private EditText txtClassName;
    private EditText txtClassID;
    private EditText txtGroupSize;
    private NachoTextView nv;
    private Button submit;
    private Professor prof;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_class, container, false);

        // TODO : Populate tags from firebase
        tags = new ArrayList<String>();
        tags.add("Java");
        tags.add("Python");
        tags.add("OOP");
        tags.add("Extra");
        tags.add("Thing");

        submit = v.findViewById(R.id.btnCreateClass);
        txtClassName = v.findViewById(R.id.txtClassEdit);
        txtGroupSize = v.findViewById(R.id.txtGroupEdit);
        txtClassID = v.findViewById(R.id.txtIDEdit);
        nv = v.findViewById(R.id.nacho_text_view);

        String uid = UserPreferences.read(UserPreferences.UID_VALUE, null);
        String loc = UserPreferences.read(UserPreferences.LOC_VALUE, null);

        User.getUserFromPref(uid, loc, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (loc.equals("Professors")) {
                    prof = dataSnapshot.getValue(Professor.class);
                    addClassListener();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        nv.setChipTokenizer(new SpanChipTokenizer<ChipSpan>(getActivity(), new ChipSpanChipCreator() {
            @Override
            public ChipSpan createChip(@NonNull Context context, @NonNull CharSequence text, Object data) {
                return new ChipSpan(context, text, ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher), data);
            }

            @Override
            public void configureChip(@NonNull ChipSpan chip, @NonNull ChipConfiguration chipConfiguration) {
                super.configureChip(chip, chipConfiguration);
            }
        }, ChipSpan.class) {
            @Override
            public CharSequence terminateToken(CharSequence text, @Nullable Object data) {
                if (!tags.contains(text.toString())) {
                    return "";
                }
                return super.terminateToken(text, data);
            }
        });

        nv.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        nv.enableEditChipOnTouch(true, true);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, tags);
        nv.setAdapter(adapter);

        return v;
    }

    public void addClassListener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wpi.jtkaplan.teamup.model.Class classToCreate = new wpi.jtkaplan.teamup.model.Class(txtClassName.getText().toString(), txtClassID.getText().toString(), prof.UID);
                prof.addClass(classToCreate);

                tags = (ArrayList<String>) nv.getChipValues();
                for (String skill: tags){
                    classToCreate.addSkill(skill);
                }

                Toast.makeText(
                        getActivity(),
                        "Class Created: " + classToCreate.getId(),
                        Toast.LENGTH_SHORT
                ).show();

            }
        });
    }
}
