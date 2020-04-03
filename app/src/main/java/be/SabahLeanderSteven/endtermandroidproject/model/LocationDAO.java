package be.SabahLeanderSteven.endtermandroidproject.model;

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

    @Query("SELECT * from locations")
    List<Location> getAllLocations();

    @Query("SELECT * FROM locations WHERE id LIKE :id")
    Location findLocationById(String id);

}
