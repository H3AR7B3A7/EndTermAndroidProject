package be.SabahLeanderSteven.endtermandroidproject;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import be.SabahLeanderSteven.endtermandroidproject.fragments.HomeFragment;
import be.SabahLeanderSteven.endtermandroidproject.fragments.ListFragment;
import be.SabahLeanderSteven.endtermandroidproject.fragments.MapFragment;

public class MainActivity extends AppCompatActivity {

    String currentDataSelected = "COMICS";

    /**
     * ON CREATE METHOD
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Components
        // NAVIGATION Component
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        // SIDEBAR Components
        NavigationView sideNavigationView = findViewById(R.id.sidebar);
        sideNavigationView.setNavigationItemSelectedListener(sidebarListener);

        // SETUP First Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, HomeFragment.newInstance("COMICS")).commit();
    }

    /**
     * SIDEBAR NAVIGATION
     */
    private NavigationView.OnNavigationItemSelectedListener sidebarListener = new NavigationView.OnNavigationItemSelectedListener(){
        /**
         * ON NAVIGATION ITEM SELECTED
         * @param item: selected menu item from sidebar
         * @return Fragment
         */
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment newHomeFragment = null;

            switch (item.getItemId()){
                case R.id.comics_pressed:
                    currentDataSelected = "COMICS";
                    newHomeFragment = HomeFragment.newInstance(currentDataSelected);
                    // TODO : Pass currentDataSelected as argument of newInstance in SIDEBAR NAVIGATION for data selection in map and list
                    break;
                case R.id.sculptures_pressed:
                    currentDataSelected = "SCULPTURES";
                    newHomeFragment = HomeFragment.newInstance(currentDataSelected);
                    break;


            }
            assert newHomeFragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, newHomeFragment).commit();
            return true;
        }
    };

    /**
     * GET THE SELECTED ITEM IN SIDEBAR (for rv and map-pins)
     * @return currentDataSelected
     */
    public String getCurrentDataSelected(){
        return currentDataSelected;
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
                    selectedFragment = HomeFragment.newInstance(currentDataSelected);
                    break;
                case R.id.nav_map:
                    selectedFragment = MapFragment.newInstance();
                    break;
                case R.id.nav_list:
                    selectedFragment = ListFragment.newInstance();
                    break;
            }

            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
            return true;
        }
    };
}
