package be.SabahLeanderSteven.endtermandroidproject.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Location.class}, version = 1, exportSchema = false)
public abstract class LocationRoomDB extends RoomDatabase {

    public abstract LocationDAO locationDAO();

    private static LocationRoomDB INSTANCE;

    static LocationRoomDB getDatabase(final Context context) {
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

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };


    // TODO : Figure out whether we need populating inner class (factory data)

    /**
     * Inner class to populate db in background (with test Location-Object)
     */
    static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {


        private final LocationDAO mDao;
        Location test = new Location(2020, "Testies", "Testers", "test.jpg", "00, 00");
        Location[] locations = {test};

        PopulateDbAsync(LocationRoomDB db) {
            mDao = db.locationDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            // mDao.deleteAll();


            for (int i = 0; i <= locations.length - 1; i++) {
                Location location = new Location(locations[i].getYear(),
                        locations[i].getCharacters(),
                        locations[i].getAuthors(),
                        locations[i].getPhoto(),
                        locations[i].getCoordinates());
                mDao.insert(location);
            }
            return null;
        }
    }
}
