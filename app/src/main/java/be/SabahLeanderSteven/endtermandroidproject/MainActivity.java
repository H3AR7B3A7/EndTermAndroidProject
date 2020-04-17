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

import be.SabahLeanderSteven.endtermandroidproject.fragments.AboutFragment;
import be.SabahLeanderSteven.endtermandroidproject.fragments.HomeFragment;
import be.SabahLeanderSteven.endtermandroidproject.fragments.ListFragment;
import be.SabahLeanderSteven.endtermandroidproject.fragments.MapFragment;
import be.SabahLeanderSteven.endtermandroidproject.fragments.MapFragment2;
import be.SabahLeanderSteven.endtermandroidproject.model.Location;
import be.SabahLeanderSteven.endtermandroidproject.model.LocationRoomDB;
import be.SabahLeanderSteven.endtermandroidproject.model.LocationViewModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String currentDataSelected = "COMICS";

    private Boolean comicsInDB;
    private Boolean graffitiInDB;

    private static final String DATA_STATE = "dataState";
    private static final String COMICS = "comics";
    private static final String GRAFFITI = "graffiti";

    private ExecutorService threadExecutor = Executors.newFixedThreadPool(4);

    /**
     * ON CREATE METHOD
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // LOAD Data States
        loadComicsDataState();
        loadGraffitiDataState();

        // Fetch Data if not present
        fetchComicsLocations(); // Fetching GRAFFITI only on sidebar navigation

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
                    break;
                case R.id.graffiti_pressed:
                    currentDataSelected = "GRAFFITI";
                    newHomeFragment = HomeFragment.newInstance(currentDataSelected);
                    fetchGraffitiLocations();
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
                case R.id.nav_info:
                    selectedFragment = AboutFragment.newInstance();
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
    private void fetchComicsLocations() {
        SharedPreferences sharedPreferences = getSharedPreferences(DATA_STATE, MODE_PRIVATE);
        if (!sharedPreferences.contains(COMICS)) {
            threadExecutor.execute(() -> {
                Log.e("DATA", "Start fetching COMICS");
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
                                (fields.has("personnage_s")) ? fields.getString("personnage_s") : "Unspecified",
                                fields.getString("auteur_s"),
                                (fields.has("photo")) ? fields.getJSONObject("photo").getString("id") : "Unspecified",
                                "COMICS",
                                fields.getJSONArray("coordonnees_geographiques").getDouble(0),
                                fields.getJSONArray("coordonnees_geographiques").getDouble(1)
                        );

                        locations.add(currentLocation);
                        LocationViewModel locationViewModel = new LocationViewModel(getApplication());
                        locationViewModel.insertLocation(currentLocation);

                        saveComicsDataState();
                    }


                } catch (IOException | JSONException exception) {
                    exception.printStackTrace();
                }
            });
        }
    }

    private void fetchGraffitiLocations(){
        SharedPreferences sharedPreferences = getSharedPreferences(DATA_STATE, MODE_PRIVATE);
        if (!sharedPreferences.contains(GRAFFITI)){
            threadExecutor.execute(()->{
                Log.e("DATA", "Start fetching GRAFFITI");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://opendata.brussel.be/api/records/1.0/search/?dataset=street-art&rows=23")
                        .get()
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                    JSONArray jsonArray = jsonObject.getJSONArray("records");

                    ArrayList<Location> locations = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject fields = jsonArray.getJSONObject(i).getJSONObject("fields");

                        final Location currentLocation = new Location(
                            (fields.has("annee")) ? Integer.parseInt(fields.getString("annee")) : 9999,
                            (fields.has("name_of_the_work")) ? fields.getString("name_of_the_work") : "Unspecified",
                            (fields.has("naam_van_de_kunstenaar")) ? fields.getString("naam_van_de_kunstenaar") : "Unspecified",
                            (fields.has("photo")) ? fields.getJSONObject("photo").getString("id") : "Unspecified",
                            "GRAFFITI",
                                fields.getJSONArray("geocoordinates").getDouble(0),
                                fields.getJSONArray("geocoordinates").getDouble(1)
                        );
                        locations.add(currentLocation);
                        LocationViewModel locationViewModel = new LocationViewModel(getApplication());
                        locationViewModel.insertLocation(currentLocation);

                        saveGraffitiDataState();
                    }

                } catch (IOException | JSONException exception) {
                    exception.printStackTrace();
                }
            });
        }
    }

    /**
     * SAVE DATA STATES for COMICS and GRAFFITI
     */
    public void saveComicsDataState(){
        comicsInDB = true;

        SharedPreferences sharedPreferences = getSharedPreferences(DATA_STATE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(COMICS, comicsInDB);
        editor.apply();
    }

    public void saveGraffitiDataState(){
        graffitiInDB = true;

        SharedPreferences sharedPreferences = getSharedPreferences(DATA_STATE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(GRAFFITI, graffitiInDB);
        editor.apply();
    }

    /**
     * LOAD DATA STATES for COMICS and GRAFFITI
     */
    public void loadComicsDataState(){
        SharedPreferences sharedPreferences = getSharedPreferences(DATA_STATE, MODE_PRIVATE);
        comicsInDB = sharedPreferences.getBoolean(COMICS, false);
    }

    public void loadGraffitiDataState(){
        SharedPreferences sharedPreferences = getSharedPreferences(DATA_STATE, MODE_PRIVATE);
        graffitiInDB = sharedPreferences.getBoolean(GRAFFITI, false);
    }
}
