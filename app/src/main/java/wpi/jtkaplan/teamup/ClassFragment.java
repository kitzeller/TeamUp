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

import com.hootsuite.nachos.ChipConfiguration;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.ChipSpan;
import com.hootsuite.nachos.chip.ChipSpanChipCreator;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.hootsuite.nachos.tokenizer.SpanChipTokenizer;

import java.util.ArrayList;

/**
 * Class for creating a class
 */

public class ClassFragment extends Fragment { // TODO :: REFACTOR THE NAME FOR CLASSFRAGMENT -- THIS IS MISLEADING?

    private ArrayList<String> tags;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_create_class, container, false);

        // TODO : Populate tags from firebase
        tags = new ArrayList<String>();
        tags.add("Java");
        tags.add("Python");
        tags.add("OOP");
        tags.add("Extra");
        tags.add("Thing");

        NachoTextView nv = v.findViewById(R.id.nacho_text_view);
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

        ArrayList<String> tags = (ArrayList<String>) nv.getChipValues();
        //TODO: add skills to class

        return v;
    }
}
