package com.example.weatherapp_p2.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.weatherapp_p2.model.SavedPlace;

import java.util.List;

@Dao
public interface SavedPlaceDAO {

    @Query("SELECT * FROM favorito WHERE userId = :userId")
    List<SavedPlace> loadAllById(String userId);

    @Query("SELECT COUNT(*) FROM favorito WHERE userId = :userId AND city = :cidade")
    int isSavedPlace(String userId, String cidade);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insterSavedPlace(SavedPlace savedPlace);

    @Delete
    void deleteSavedPlace(SavedPlace savedPlace);

}
