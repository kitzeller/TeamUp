package wpi.jtkaplan.teamup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private String loc;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uid = UserPreferences.read(UserPreferences.UID_VALUE,null);
        loc = UserPreferences.read(UserPreferences.LOC_VALUE,null);
        System.out.println("MainActivity Loading " + uid + " " + loc);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new HomeFragment()).commit();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).addToBackStack(null).commit();
        }
    }

    // This controls the ButtonNavigation view
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    // Select Fragments based on which tab is selected
                    switch (item.getItemId()) {
//                        case R.id.nav_home:
//                            //selectedFragment = new HomeFragment();
//                            // TODO: Add funtionality for different home screen depending on if Profesor or Student
//                            // todo: let's discuss the above point -- might not need to do this.
//                            selectedFragment = new CreateClassFragment();
//                            break;
                        case R.id.nav_groups:
                            if (loc.equals("Students")){
                                selectedFragment = new ClassesRecyclerViewFragment();
                            } else {
                                selectedFragment = new ClassesRecyclerViewFragment();
                            }
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).addToBackStack(null).commit();

                    return true;
                }
            };
}