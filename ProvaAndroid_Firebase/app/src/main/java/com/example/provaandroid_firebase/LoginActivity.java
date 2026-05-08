package com.example.provaandroid_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextView tv_go_to_register;
    private EditText et_login_password, et_login_email;
    private ImageButton ivTogglePassword;
    private FirebaseAuth aluno = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        tv_go_to_register = findViewById(R.id.tv_go_to_register);
        ivTogglePassword = findViewById(R.id.iv_toggle_login_password);
        et_login_password = findViewById(R.id.et_login_password);
        et_login_email = findViewById(R.id.et_login_email);

        FirebaseUser logged_user = aluno.getCurrentUser();

        if(logged_user != null) {
            navegaTelaMain();
            Log.i("CURRENTUSER", logged_user.getUid());
        }

        // Toggle de visibilidade da senha
        ivTogglePassword.setOnClickListener(v -> toggleSenha(et_login_password, ivTogglePassword));

        btn_login.setOnClickListener(v -> {
            login(v);
        });

        tv_go_to_register.setOnClickListener(v -> navegaTelaCadastro());

    }
    private void navegaTelaMain(){
        Intent telaMain = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(telaMain);
    }
    private void navegaTelaCadastro(){
        Intent telaCadastro = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(telaCadastro);
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

    private void login(View v){
        String email = et_login_email.getText().toString();
        String password = et_login_password.getText().toString();

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        aluno.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            String erro;
                            try {
                                throw  task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                erro = "Email não existe ou a conta foi desativada.";
                            } catch (FirebaseNetworkException e) {
                                erro = "Dispositivo offline.";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "Senha incorreta ou formato do e-mail inválido";
                            } catch (Exception e) {
                                erro = "Erro ao tentar fazer o login do usuário!";
                            }
                            Toast.makeText(LoginActivity.this, erro, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        navegaTelaMain();
                        finish();

                    }
                });

    }

}