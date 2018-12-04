package wpi.jtkaplan.teamup;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new HomeFragment()).commit();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ClassFragment()).commit();
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
                        case R.id.nav_home:
                            //selectedFragment = new HomeFragment();
                            // TODO: Add funtionality for different home screen depending on if Profesor or Student
                            selectedFragment = new ClassFragment();
                            break;
                        case R.id.nav_favorites:
                            selectedFragment = new RecyclerViewFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}