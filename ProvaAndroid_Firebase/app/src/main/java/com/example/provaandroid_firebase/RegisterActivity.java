package com.example.provaandroid_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.text.InputType;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.provaandroid_firebase.model.Aluno;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth aluno = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private  DatabaseReference reference = database.getReference();
    private Toolbar toolbar;
    private EditText et_ra, et_name, et_email, et_password, et_password_confirm;
    private ImageButton iv_toggle_password, iv_toggle_password_confirm;
    private Button btn_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        et_ra = findViewById(R.id.et_ra);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_password_confirm = findViewById(R.id.et_password_confirm);
        iv_toggle_password = findViewById(R.id.iv_toggle_password);
        iv_toggle_password_confirm = findViewById(R.id.iv_toggle_password_confirm);
        toolbar = findViewById(R.id.toolbar_register);
        btn_save = findViewById(R.id.btn_save);

        // Toggle de visibilidade da senha
        iv_toggle_password.setOnClickListener(v -> toggleSenha(et_password, iv_toggle_password));
        iv_toggle_password_confirm.setOnClickListener(v -> toggleSenha(et_password_confirm, iv_toggle_password_confirm));

        //Registro
        btn_save.setOnClickListener(v -> {
            String ra = et_ra.getText().toString();
            String nome = et_name.getText().toString();
            String email = et_email.getText().toString();
            String senha = et_password.getText().toString();
            String senha_confirma = et_password_confirm.getText().toString();

            if(ra.isEmpty() || nome.isEmpty() || email.isEmpty() || senha.isEmpty() || senha_confirma.isEmpty()){
                Toast.makeText(RegisterActivity.this,"Necessário preencher todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senha.equals(senha_confirma)) {
                Toast.makeText(RegisterActivity.this, "Senhas diferentes!", Toast.LENGTH_SHORT).show();
                return;
            }

            cadastrarAluno(ra, nome, email, senha);

        });

        // Registra como a ActionBar desta Activity
        setSupportActionBar(toolbar);

        // Ativa a seta de voltar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // Captura o clique na seta
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // fecha esta Activity e volta para a anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleSenha(EditText campo, ImageButton botao) {
        int inputAtual = campo.getInputType();
        int senhaOculta  = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        int senhaVisivel = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

        if (inputAtual == senhaOculta) {
            // Estava oculta → mostra
            campo.setInputType(senhaVisivel);
            botao.setImageResource(android.R.drawable.ic_menu_view);
        } else {
            // Estava visível → oculta
            campo.setInputType(senhaOculta);
            botao.setImageResource(R.drawable.ic_eye_toggle);
        }

        campo.setSelection(campo.getText().length());
    }

    private void cadastrarAluno(String ra, String nome, String email, String senha){
        aluno.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            String erro;
                            try{
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erro = "Digite uma senha com no mínimo 6 caracteres";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erro = "E-mail já cadastrado";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "E-mail inválido";
                            } catch (Exception e) {
                                erro = "Erro ao cadastra aluno!";
                            }
                            Toast.makeText(RegisterActivity.this, erro, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        FirebaseUser firebaseUser = aluno.getCurrentUser();
                        if (firebaseUser == null) {
                            Log.e("DEBUG", "Tentativa de gravar dados sem usuário logado!");
                        }
                        String uid = firebaseUser.getUid();
                        //salva com validação de RA condicional
                        salvarDadosAluno(uid, ra, nome, email);
                    }
                });
    }

    private void navegaTelaLogin(){
        Intent telaLogin = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(telaLogin);
        aluno.signOut();
    }

    private void salvarDadosAluno(String uid, String ra, String nome, String email){
        DatabaseReference alunos = reference.child("alunos");
        DatabaseReference raRef = reference.child("ras_ocupados").child(ra);

        raRef.setValue(uid).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Aluno alunoDados = new Aluno();
                alunoDados.setRa(ra);
                alunoDados.setNome(nome);
                alunoDados.setEmail(email);

                alunos.child(uid).setValue(alunoDados).addOnCompleteListener(taskSave -> {
                    Toast.makeText(RegisterActivity.this, "Sucesso ao cadastrar!", Toast.LENGTH_SHORT).show();
                    navegaTelaLogin();
                });

            } else {
                if (aluno.getCurrentUser() != null) {
                    aluno.getCurrentUser().delete().addOnCompleteListener(deleteTask -> {
                        Toast.makeText(RegisterActivity.this, "RA já pertence a outro aluno!", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

    }

}