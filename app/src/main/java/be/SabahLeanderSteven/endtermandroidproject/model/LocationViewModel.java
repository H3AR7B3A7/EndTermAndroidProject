package be.SabahLeanderSteven.endtermandroidproject.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

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
