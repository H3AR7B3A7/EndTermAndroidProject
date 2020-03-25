package be.SabahLeanderSteven.endtermandroidproject.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Location.class}, version = 1, exportSchema = false)
public abstract class LocationRoomDB extends RoomDatabase {

    public abstract LocationDAO locationDAO();
    private static LocationRoomDB INSTANCE;

    static LocationRoomDB getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (LocationRoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocationRoomDB.class, "location_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
//            new PopulateDbAsync(INSTANCE).execute();
        }
    };


    // TODO : Figure out whether populating inner class is needed when fetching data in LocationViewModel
//    /**
//     * Inner class to populate db in background
//     */
//    static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
//
//
//        private final LocationDAO mDao;
//        // ?? Data binnenhalen
//        Location[] locations = {};
//        private MutableLiveData<Location> comicBookLocations;
//
//        PopulateDbAsync(LocationRoomDB db){
//            mDao = db.locationDAO();
//        }
//
//        @Override
//        protected Void doInBackground(final Void... params) {
//            // Start the app with a clean database every time.
//            // Not needed if you only populate the database
//            // when it is first created
//            // mDao.deleteAll();
//
//
//            for (int i = 0; i <= locations.length - 1; i++) {
//                int year;
//                // ??? Data
//                String characters, authors, photo, coordinates;
//                Location location = new Location(locations[i], year, characters, authors, photo, coordinates);
//                mDao.insert(location);
//            }
//            return null;
//        }
//    }
}
