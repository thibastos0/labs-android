package com.example.provaandroid_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button btn_grades, btn_register, btn_logout;
    private FirebaseAuth aluno = FirebaseAuth.getInstance();
    private GoogleSignInClient googleClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btn_grades = findViewById(R.id.btn_grades);
        btn_register = findViewById(R.id.btn_register);
        btn_logout = findViewById(R.id.btn_logout);

        btn_grades.setOnClickListener(v -> navegaTelaGrades());
        btn_register.setOnClickListener(v -> navegaTelaCadastro());
        btn_logout.setOnClickListener(v -> sair());

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN
                )
                        .requestEmail()
                        .build();

        googleClient = GoogleSignIn.getClient(this, signInOptions);
    }

    private void navegaTelaGrades(){
        Intent telaGrades = new Intent(MainActivity.this, GradesActivity.class);
        startActivity(telaGrades);
    }
    private void navegaTelaCadastro(){
        Intent telaCadastro = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(telaCadastro);
    }

    private void sair(){
        telaLogin();
    }

    private void telaLogin(){
        Intent telaLogin = new Intent(MainActivity.this, LoginActivity.class);
        telaLogin.addFlags(telaLogin.FLAG_ACTIVITY_NEW_TASK | telaLogin.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(telaLogin);
        aluno.signOut();

        googleClient.signOut().addOnCompleteListener(task -> {
            finish();
        });

        finish();
    }
}