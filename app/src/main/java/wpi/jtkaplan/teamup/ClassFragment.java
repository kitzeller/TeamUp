package wpi.jtkaplan.teamup;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.util.ArrayList;

/**
 * Class for creating a class
 */

public class ClassFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_create_class, container, false);

        NachoTextView nv = v.findViewById(R.id.nacho_text_view);
        nv.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        nv.enableEditChipOnTouch(true, true);

        ArrayList<String> tags = (ArrayList<String>) nv.getChipValues();

        return v;
    }
}
