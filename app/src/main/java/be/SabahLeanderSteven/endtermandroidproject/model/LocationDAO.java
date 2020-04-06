package be.SabahLeanderSteven.endtermandroidproject.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDAO {

    @Insert
    void insert(Location location);

    @Query("DELETE FROM locations")
    void deleteAll();

    //vermijd het zelf aanspreken van getters etc., sowieso al geobserveerd
    //livedata werkt enkel met List, niet met ArrayList
    @Query("SELECT * from locations")
    LiveData<List<Location>> getAllLocations();

    @Query("SELECT * FROM locations WHERE id LIKE :id")
    Location findLocationById(String id);

}
