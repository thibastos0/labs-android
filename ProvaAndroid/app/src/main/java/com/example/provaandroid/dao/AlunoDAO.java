package com.example.provaandroid.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.provaandroid.database.DatabaseConnection;
import com.example.provaandroid.model.Aluno;

public class AlunoDAO {

    private SQLiteDatabase db;
    private final DatabaseConnection con;

    public AlunoDAO(Context context){
        con = new DatabaseConnection(context);
    }

    //INSERT
    public boolean insertUser(Aluno aluno) {
        db = con.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ra", aluno.getRa());
        values.put("nome", aluno.getNome());
        values.put("email", aluno.getEmail());

        long result = db.insert(con.TABELA_ALUNO, null, values);
        return result != -1;
    }


}
