package be.SabahLeanderSteven.endtermandroidproject.model;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationViewModel extends AndroidViewModel {

    private LocationRepository mRepository;
    private ExecutorService threadExecutor = Executors.newFixedThreadPool(4);

    public LocationViewModel(Application application) {
        super(application);
        mRepository = new LocationRepository(application);
    }

    public LiveData<List<Location>> getAllLocations() {
        return mRepository.getAllLocations();
    }

    public LiveData<List<Location>> getAllLocationsOfType(String type) {
        return mRepository.getAllLocationsOfType(type);
    }

    public void insertLocation(Location location) {
        mRepository.insert(location);
    }

    public Location findLocationById(String id) {
        return LocationRoomDB.getDatabase(getApplication()).locationDAO().findLocationById(id);
    }

    public void deleteAllLocations() {
        LocationRoomDB.getDatabase(getApplication()).locationDAO().deleteAll();
    }



    // live data binnehalen
    public LiveData<List<Location>> getLocation() {
        fetchLocation();
        return mRepository.getAllLocations();}


        // via Json data binnenhalen
        private void fetchLocation () {
            threadExecutor.execute(new Runnable() {
                @Override
                public void run() {
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
                                    fields.getJSONArray("coordonnees_geographiques").getDouble(0),
                                    fields.getJSONArray("coordonnees_geographiques").getDouble(1)
                            );
                            locations.add(currentLocation);
                            LocationViewModel locationViewModel = new LocationViewModel(getApplication());
                            locationViewModel.insertLocation(currentLocation);

                            //saveGraffitiDataState();
                        }
                    } catch (IOException | JSONException exception) {
                        exception.printStackTrace();
                    }
                }
            });
        }
    }
