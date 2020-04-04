package be.SabahLeanderSteven.endtermandroidproject;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import be.SabahLeanderSteven.endtermandroidproject.fragments.HomeFragment;
import be.SabahLeanderSteven.endtermandroidproject.fragments.ListFragment;
import be.SabahLeanderSteven.endtermandroidproject.fragments.MapFragment;

public class MainActivity extends AppCompatActivity {

    /**
     * ON CREATE METHOD
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NAVIGATION
        // UI Component references
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        // Setup
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
    }

    /**
     * BOTTOM NAVIGATION
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        /**
         * ON NAVIGATION ITEM SELECTED
         * @param item: selected menu item from bottom_navigation
         * @return Fragment
         */
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_map:
                    selectedFragment = new MapFragment();
                    break;
                case R.id.nav_list:
                    selectedFragment = new ListFragment();
                    break;
            }

            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
            return true;
        }
    };
}
