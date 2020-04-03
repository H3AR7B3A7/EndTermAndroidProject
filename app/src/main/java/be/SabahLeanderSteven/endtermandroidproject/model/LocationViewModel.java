package be.SabahLeanderSteven.endtermandroidproject.model;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationViewModel extends AndroidViewModel {

    // TODO : make recyclerview or pins on map to getAllLocations from mRepository so this code actually runs. And match the SysOut to the object.

    private LocationRepository mRepository;
    private List<Location> mAllLocations;
    private ExecutorService threadExecutor = Executors.newFixedThreadPool(4);

    public LocationViewModel(Application application) {
        super(application);
        mRepository = new LocationRepository(application);
        mAllLocations = mRepository.getAllLocations();

    }

    public List<Location> getAllLocations() {
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

                System.out.println(jsonObject.toString());

                //TODO : Dit hieronder deftig uitzoeken

                int year = jsonObject.getInt("annee");
                String characters = jsonObject.getString("personage_s");
                String authors = jsonObject.getString("auteur_s");
                String coordinates = jsonObject.getString("coordinates");
                String photo = jsonObject.getString("filename");

                Location location = new Location(year, characters, authors, coordinates, photo);

                insertLocation(location);

            } catch (IOException | JSONException exception) {
                exception.printStackTrace();
            }
        });
    }
}
