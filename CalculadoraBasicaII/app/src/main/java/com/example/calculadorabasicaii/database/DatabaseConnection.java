package com.example.calculadorabasicaii.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnection extends SQLiteOpenHelper {
    private static final String DB_NAME = "calculadora.db";
    private static final int DB_VERSION = 1;

    public static final String TABELA_USER="user";
    public static final String TABELA_CALCMEMO="calcmemo";

    public DatabaseConnection(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABELA_USER + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "mail TEXT UNIQUE," +
                "password TEXT)";
        db.execSQL(sql);

        sql = "CREATE TABLE " + TABELA_CALCMEMO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "calculo TEXT," +
                "display TEXT," +
                "FOREIGN KEY (userId) REFERENCES " + TABELA_USER + "(id))";
        db.execSQL(sql);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_CALCMEMO);
        onCreate(db);

    }


}
