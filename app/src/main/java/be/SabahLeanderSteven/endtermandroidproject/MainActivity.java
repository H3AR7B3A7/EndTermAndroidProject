package be.SabahLeanderSteven.endtermandroidproject;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import be.SabahLeanderSteven.endtermandroidproject.fragments.HomeFragment;
import be.SabahLeanderSteven.endtermandroidproject.fragments.ListFragment;
import be.SabahLeanderSteven.endtermandroidproject.fragments.MapFragment;

public class MainActivity extends AppCompatActivity {

//    private AppBarConfiguration appBarConfiguration;
//    private NavController navController;
    private Dialog myDialog;

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
        myDialog = new Dialog(this);
    }

    public void showPopup(View v){
        // UI Components references
        ImageView logoIV = findViewById(R.id.logo_iv);
        ImageButton aboutBTN = findViewById(R.id.about_ib);

        myDialog.setContentView(R.layout.about_popup);
        myDialog.show();
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

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
            return true;
        }
    };

//    @Override
//    public boolean onSupportNavigateUp() {
//        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
//    }
}
