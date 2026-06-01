package com.example.weatherapp_p2.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weatherapp_p2.model.SavedPlace;

import java.util.List;

@Dao
public interface SavedPlaceDAO {

    @Query("SELECT * FROM favorito WHERE userId = :userId AND isActive = 1")
    List<SavedPlace> loadAllById(String userId);

    @Query("SELECT * FROM favorito WHERE userId = :userId AND city = :cidade LIMIT 1")
    SavedPlace getSavedPlace(String userId, String cidade);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSavedPlace(SavedPlace savedPlace);

    @Delete
    void deleteSavedPlace(SavedPlace savedPlace);

    @Update
    void updateSavedPlace(SavedPlace savedPlace);

}
