package com.example.provaandroid;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.provaandroid.dao.AlunoDAO;
import com.example.provaandroid.model.Aluno;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextRA, editTextNome, editTextEmail;
    Button btnSalvarSQLite;
    private AlunoDAO alunoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        alunoDAO = new AlunoDAO(this);

        editTextRA = findViewById(R.id.editTextRA);
        editTextNome = findViewById(R.id.editTexNome);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        btnSalvarSQLite = findViewById(R.id.btnSalvarSQLite);


        btnSalvarSQLite.setOnClickListener(view -> {
            String ra = editTextRA.getText().toString();
            String nome = editTextNome.getText().toString();
            String email = editTextEmail.getText().toString();
            boolean sucess = false;

            if (ra.isEmpty() || nome.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Aluno aluno = new Aluno(ra, nome, email);
                sucess = alunoDAO.insertUser(aluno);
            }

            if (!sucess) {
                Toast.makeText(this, "Erro ao cadastrar aluno", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Aluno cadastrado com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        });


    }
}