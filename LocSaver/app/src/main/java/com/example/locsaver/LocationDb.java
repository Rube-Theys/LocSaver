package com.example.locsaver;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.locsaver.DAO.LocationDAO;
import com.example.locsaver.models.Location;

@Database(entities = {Location.class}, version = 2, exportSchema = false)
public abstract class LocationDb extends RoomDatabase {

    private static LocationDb minstance;
    private static final String DB_NAME = "location.db";

    public abstract LocationDAO getLocationDAO();

    public static synchronized LocationDb getInstance(Context ctx) {
        if (minstance == null) {
            minstance = Room.databaseBuilder(ctx.getApplicationContext(), LocationDb.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return minstance;
    }
}