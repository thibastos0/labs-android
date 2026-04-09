package com.example.calculadorabasicaii.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.calculadorabasicaii.database.DatabaseConnection;
import com.example.calculadorabasicaii.model.User;

public class UserDAO {
    private SQLiteDatabase db;
    private final DatabaseConnection con;

    public UserDAO(Context context){
        con = new DatabaseConnection(context);
    }

    //INSERT
    public boolean insertUser(User user) {
        db = con.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("mail", user.getMail());
        values.put("password", user.getPassword());

        long result = db.insert(DatabaseConnection.TABELA_USER, null, values);
        return result != -1;
    }

    //LOGIN
    public int login(String mail, String password) {
        db = con.getReadableDatabase();

        String sql = "SELECT * FROM " +
                DatabaseConnection.TABELA_USER +
                " WHERE mail = ? AND password = ?";

        Cursor cursor = db.rawQuery(sql,
                new String[]{mail, password}
        );

        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();

        return id;
    }

    public String getUserNameById(int userId) {
        db = con.getReadableDatabase();

        String sql = "SELECT name FROM " +
                DatabaseConnection.TABELA_USER +
                " WHERE id = ?";

        Cursor cursor = db.rawQuery(sql,
                new String[]{String.valueOf(userId)}
        );

        String name = "";
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        }
        cursor.close();

        return name;
    }


}
