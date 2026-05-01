package com.example.provaandroid;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.provaandroid.database.AppDatabase;
import com.example.provaandroid.model.Aluno;

import java.util.concurrent.Executors;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextRA, editTextNome, editTextEmail;
    Button btnSalvarSQLite;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        db = AppDatabase.getInstance(this);

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
            }

            Aluno aluno = new Aluno(ra, nome, email);

            // Cria e Insere em uma linha só (dentro da Thread)
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    // Tenta inserir o aluno
                    db.alunoDAO().insertAluno(aluno);

                    // Avisa o usuário na Thread principal
                    runOnUiThread(() -> {
                        // Se chegou aqui, deu certo! Volta para a UI para avisar
                        Toast.makeText(this, "Aluno cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } catch (SQLiteConstraintException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Erro: Este RA já está cadastrado!", Toast.LENGTH_SHORT).show();
                    });
                } catch (Exception e) {
                    // Se algo deu errado (ex: RA duplicado, erro de disco), cai aqui
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Erro ao cadastrar aluno: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });

    }
}