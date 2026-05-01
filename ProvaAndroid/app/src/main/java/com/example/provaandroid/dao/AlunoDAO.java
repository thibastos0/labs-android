package com.example.provaandroid.dao;

import android.content.ContentValues;
import android.content.Context;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.provaandroid.model.Aluno;

import java.util.List;

@Dao
public interface AlunoDAO {

    @Query("SELECT * FROM aluno")
    List<Aluno> getAll();

    @Query("SELECT * FROM aluno WHERE id = :id")
    Aluno findById(int id);

    @Insert
    void insertAluno(Aluno aluno);

    @Delete
    void deleteAluno(Aluno aluno);
}
