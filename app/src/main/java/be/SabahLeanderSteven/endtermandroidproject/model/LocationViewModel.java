package be.SabahLeanderSteven.endtermandroidproject.model;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {

    // TODO : Set up request with OkHttpClient to fetch data from api and save it in room db

    private LocationRepository mRepository;
    private LiveData<List<Location>> mAllLocations;

    public LocationViewModel(Application application){
        super(application);
        mRepository = new LocationRepository(application);
        mAllLocations = mRepository.getAllLocations();
    }

    LiveData<List<Location>> getmAllLocations(){
        return mAllLocations;
    }

    public void insert(Location location){
        mRepository.insert(location);
    }
}
