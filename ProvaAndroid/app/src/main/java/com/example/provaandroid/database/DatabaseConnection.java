package com.example.provaandroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnection extends SQLiteOpenHelper {
    private static final String DB_NAME = "provaAndroid.db";
    private static final int DB_VERSION = 1;

    public static final String TABELA_ALUNO="alunos";

    public DatabaseConnection(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABELA_ALUNO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ra TEXT NOT NULL," +
                "nome TEXT NOT NULL," +
                "email TEXT NOT NULL)";
        db.execSQL(sql);


    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ALUNO);
        onCreate(db);

    }


}