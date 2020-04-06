package be.SabahLeanderSteven.endtermandroidproject.model;

import android.app.Application;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationViewModel extends AndroidViewModel {

    private LocationRepository mRepository;

    public LocationViewModel(Application application) {
        super(application);
        mRepository = new LocationRepository(application);
    }

    public LiveData<List<Location>> getAllLocations() {
        // Check if not already present before fetching??
        return mRepository.getAllLocations();
    }


    private void insertLocation(Location location) {
        mRepository.insert(location);
    }

    public Location findLocationById(String id) {
        return LocationRoomDB.getDatabase(getApplication()).locationDAO().findLocationById(id);
    }

    public void deleteAllLocations() {
        LocationRoomDB.getDatabase(getApplication()).locationDAO().deleteAll();
    }



}
