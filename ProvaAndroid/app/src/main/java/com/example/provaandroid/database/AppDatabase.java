package com.example.provaandroid.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.provaandroid.dao.AlunoDAO;
import com.example.provaandroid.model.Aluno;

@Database(entities = {Aluno.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase { // Deve ser abstract

    // Método abstrato que retorna o DAO
    public abstract AlunoDAO alunoDAO();

    private static volatile AppDatabase INSTANCE;

    /*
    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "prova_db").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }*/

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "prova_db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;

    }

}