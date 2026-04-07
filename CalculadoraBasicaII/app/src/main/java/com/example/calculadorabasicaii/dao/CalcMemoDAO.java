package com.example.calculadorabasicaii.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.calculadorabasicaii.database.DatabaseConnection;
import com.example.calculadorabasicaii.model.CalcMemo;

public class CalcMemoDAO {
    private SQLiteDatabase db;
    private final DatabaseConnection con;

    public CalcMemoDAO(Context context){
        con = new DatabaseConnection(context);
    }

    //save
    private boolean saveCalcMemo(CalcMemo calcMemo) {
        db = con.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("userId", calcMemo.getUserId());
        values.put("calculo", calcMemo.getCalculo());
        values.put("display", calcMemo.getDisplay());

        long result = db.insert(DatabaseConnection.TABELA_CALCMEMO, null, values);
        return result != -1;
    }

    //Find
    public CalcMemo findCalcMemo(int userId) {
        db = con.getReadableDatabase();
        String sql = "SELECT * FROM " +
                DatabaseConnection.TABELA_CALCMEMO +
                " WHERE userId = ?";
        Cursor cursor = db.rawQuery(sql,
                new String[]{String.valueOf(userId)}
        );
        int result = -1;
        cursor.moveToFirst();
        if (cursor.getCount() <= 0) {
            cursor.close();
            this.saveCalcMemo(new CalcMemo(userId, "0", "0"));
            return null;
        }
        CalcMemo calcMemo = new CalcMemo();
        calcMemo.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        calcMemo.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("userId")));
        calcMemo.setCalculo(cursor.getString(cursor.getColumnIndexOrThrow("calculo")));
        calcMemo.setDisplay(cursor.getString(cursor.getColumnIndexOrThrow("display")));
        cursor.close();
        return calcMemo;
    }

    //update
    public boolean updateCalcMemo(CalcMemo calcMemo) {
        db = con.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("userId", calcMemo.getUserId());
        values.put("calculo", calcMemo.getCalculo());
        values.put("display", calcMemo.getDisplay());

        String where = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(calcMemo.getId())};

        int result = db.update(DatabaseConnection.TABELA_CALCMEMO, values, where, whereArgs);
        return result != -1;
    }


}
