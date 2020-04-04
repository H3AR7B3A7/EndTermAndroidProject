package be.SabahLeanderSteven.endtermandroidproject.model;

import android.app.Application;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationViewModel extends AndroidViewModel {

    private LocationRepository mRepository;
    private LiveData<ArrayList<Location>> mAllLocations;
    private ExecutorService threadExecutor = Executors.newFixedThreadPool(4);

    public LocationViewModel(Application application) {
        super(application);
        mRepository = new LocationRepository(application);
        mAllLocations = mRepository.getAllLocations();

    }

    public LiveData<ArrayList<Location>> getAllLocations() {
        // Check if not already present before fetching??
        fetchComicBookLocations();

        return mAllLocations;
    }



    private void insertLocation(Location location) {
        mRepository.insert(location);
    }

    public List<Location> getAllLocationsFromDB(){
        return LocationRoomDB.getDatabase(getApplication()).locationDAO().getAllLocations();
    }

    public Location findLocationById(String id){
        return LocationRoomDB.getDatabase(getApplication()).locationDAO().findLocationById(id);
    }

    public void deleteAllLocations(){
        LocationRoomDB.getDatabase(getApplication()).locationDAO().deleteAll();
    }



    private void fetchComicBookLocations() {
        threadExecutor.execute(() -> {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://bruxellesdata.opendatasoft.com/api/records/1.0/search/?dataset=comic-book-route")
                    .get().build();

            try {
                Response response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                JSONArray jsonArray = jsonObject.getJSONArray("records");

                //TODO : Dit hieronder deftig uitzoeken

                ArrayList<Location> locations = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject fields = jsonArray.getJSONObject(i).getJSONObject("fields");

                    final Location currentLocation = new Location(
                            Integer.parseInt(fields.getString("annee")),
                            fields.getString("personnage_s"),
                            fields.getString("auteur_s"),
                            fields.getJSONObject("photo").getString("filename"),
                            fields.getString("coordonnees_geographiques")
                    );

                    locations.add(currentLocation);
                    LocationRoomDB.getDatabase(getApplication()).locationDAO().insert(currentLocation);
                }


            } catch (IOException | JSONException exception) {
                exception.printStackTrace();
            }
        });
    }
}
