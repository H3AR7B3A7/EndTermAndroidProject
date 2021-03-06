package be.SabahLeanderSteven.endtermandroidproject.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

class LocationRepository {

    private LocationDAO mLocationDao;

    LocationRepository(Application application) {
        LocationRoomDB db = LocationRoomDB.getDatabase(application);
        mLocationDao = db.locationDAO();
    }

    LiveData<List<Location>> getAllLocations() {
        return mLocationDao.getAllLocations();
    }

    LiveData<List<Location>> getAllLocationsOfType(String type) {
        return mLocationDao.getAllLocationsOfType(type);
    }

    void insert(Location location) {
        new insertAsyncTask(mLocationDao).execute(location);
    }

    private static class insertAsyncTask extends AsyncTask<Location, Void, Void> {

        private LocationDAO mAcyncTaskDao;

        insertAsyncTask(LocationDAO dao) {
            mAcyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Location... params) {
            mAcyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
