package com.example.weatherapp_p2.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.weatherapp_p2.dao.SavedPlaceDAO;
import com.example.weatherapp_p2.model.SavedPlace;

@Database(entities = {SavedPlace.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SavedPlaceDAO savedPlaceDAO();
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "favorito"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;

    }
}
