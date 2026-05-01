package com.example.provaandroid.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "aluno",
indices = {@Index(value = {"ra"}, unique = true)}
)
public class Aluno {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "ra")
    private String ra;
    @ColumnInfo(name = "nome")
    private String nome;
    @ColumnInfo(name = "email")
    private String email;

    public Aluno(String ra, String nome, String email) {
        this.ra = ra;
        this.nome = nome;
        this.email = email;
    }

    @Ignore
    public Aluno() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
