package wpi.jtkaplan.teamup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;


public class HomeFragment extends Fragment {


    private static final String TAG = "HomeFrag";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) v.findViewById(R.id.container);
        setupViewPager(mViewPager);

        android.support.v7.widget.Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle(UserPreferences.getSelectedClass().getName());

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        if (UserPreferences.read(UserPreferences.LOC_VALUE, null).equals(UserPreferences.STUDENT)) {
            // If Student
            SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
            adapter.addFragment(new ProfileNonEditableFragment(), "Professor");
            adapter.addFragment(new GroupRecyclerViewFragment(), "Group");
            adapter.addFragment(new StudentsRecyclerViewFragment(), "Students");
            viewPager.setAdapter(adapter);
        } else {
            // If Professor
            SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
            adapter.addFragment(new StudentsRecyclerViewFragment(), "Students");
            adapter.addFragment(new ClassGroupsRecyclerViewFragment(), "Groups");
            viewPager.setAdapter(adapter);
        }
    }
}