package com.example.locsaver.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.locsaver.models.Location;

import java.util.List;

@Dao
public interface LocationDAO {
    @Insert
    void insertLocation(Location location);

    @Update
    void updateLocation(Location location);

    @Delete
    void deleteLocation(Location location);

    @Query("SELECT * FROM Location")
    List<Location> getAllLocations();
}
