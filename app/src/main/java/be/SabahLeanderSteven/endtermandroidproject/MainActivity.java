package be.SabahLeanderSteven.endtermandroidproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.SabahLeanderSteven.endtermandroidproject.fragments.HomeFragment;
import be.SabahLeanderSteven.endtermandroidproject.fragments.ListFragment;
import be.SabahLeanderSteven.endtermandroidproject.fragments.MapFragment;
import be.SabahLeanderSteven.endtermandroidproject.model.Location;
import be.SabahLeanderSteven.endtermandroidproject.model.LocationRoomDB;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String currentDataSelected = "COMICS";

    private Boolean comicsInDB;
    private Boolean sculpturesInDB;

    private static final String DATA_STATE = "dataState";
    private static final String COMICS = "comics";
    private static final String SCULPTURES = "sculptures";

    private ExecutorService threadExecutor = Executors.newFixedThreadPool(4);

    /**
     * ON CREATE METHOD
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadComicsDataState();
        loadSculpturesDataState();

        fetchComicBookLocations();

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
                    selectedFragment = MapFragment.newInstance(currentDataSelected);
                    break;
                case R.id.nav_list:
                    selectedFragment = ListFragment.newInstance(currentDataSelected);
                    break;
            }

            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
            return true;
        }
    };

    /**
     * Async tasks to start DL in background ...
     */
    // TODO : Check if data is already present first
    private void fetchComicBookLocations() {
        SharedPreferences sharedPreferences = getSharedPreferences(DATA_STATE, MODE_PRIVATE);
        if (!sharedPreferences.contains(COMICS)) {
            threadExecutor.execute(() -> {
                Log.e("DATA", "Start fetching");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://bruxellesdata.opendatasoft.com/api/records/1.0/search/?dataset=comic-book-route&rows=58")
                        .get().build();

                try {
                    Response response = client.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                    JSONArray jsonArray = jsonObject.getJSONArray("records");

                    ArrayList<Location> locations = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject fields = jsonArray.getJSONObject(i).getJSONObject("fields");

                        final Location currentLocation = new Location(
                                Integer.parseInt(fields.getString("annee")),
                                (fields.has("personnage_s")) ? fields.getString("personnage_s") : "Unspecified", //controle of iets ingevuld
                                fields.getString("auteur_s"),
                                (fields.has("photo")) ? fields.getJSONObject("photo").getString("id") : "Unspecified",
                                fields.getString("coordonnees_geographiques")
                        );

                        locations.add(currentLocation);
                        LocationRoomDB.getDatabase(Objects.requireNonNull(this).getApplication()).locationDAO().insert(currentLocation);

                        saveComicsDataState();
                    }


                } catch (IOException | JSONException exception) {
                    exception.printStackTrace();
                }
            });
        }
    }

    public void saveComicsDataState(){
        comicsInDB = true;

        SharedPreferences sharedPreferences = getSharedPreferences(DATA_STATE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(COMICS, comicsInDB);
        editor.apply();
    }

    public void saveSculpturesDataState(){
        sculpturesInDB = true;

        SharedPreferences sharedPreferences = getSharedPreferences(DATA_STATE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SCULPTURES, sculpturesInDB);
        editor.apply();
    }

    public void loadComicsDataState(){
        SharedPreferences sharedPreferences = getSharedPreferences(DATA_STATE, MODE_PRIVATE);
        comicsInDB = sharedPreferences.getBoolean(COMICS, false);
    }

    public void loadSculpturesDataState(){
        SharedPreferences sharedPreferences = getSharedPreferences(DATA_STATE, MODE_PRIVATE);
        sculpturesInDB = sharedPreferences.getBoolean(SCULPTURES, false);
    }
}
