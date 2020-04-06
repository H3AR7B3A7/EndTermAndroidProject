package be.SabahLeanderSteven.endtermandroidproject.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class LocationRepository {

    private LocationDAO mLocationDao;
    private LiveData<ArrayList<Location>> mAllLocations;

    LocationRepository(Application application){
        LocationRoomDB db = LocationRoomDB.getDatabase(application);
        mLocationDao = db.locationDAO();
        //TODO: WTF is da me die live data en (array)lists???  Ik blijf mar heen en weer gaan tussen de twee, ik weet het ook ni.
        mAllLocations = (LiveData<ArrayList<Location>>) mLocationDao.getAllLocations();
    }

    LiveData<ArrayList<Location>> getAllLocations(){
        return mAllLocations;
    }

    public void insert(Location location){
        new insertAsyncTask(mLocationDao).execute(location);
    }

    private static class insertAsyncTask extends AsyncTask<Location, Void, Void>{

        private LocationDAO mAcyncTaskDao;

        insertAsyncTask(LocationDAO dao){
            mAcyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Location... params) {
            mAcyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
