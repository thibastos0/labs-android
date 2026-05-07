package com.example.provaandroid_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    TextView tv_go_to_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        tv_go_to_register = findViewById(R.id.tv_go_to_register);

        btn_login.setOnClickListener(v -> {
            navegaTelaMain();
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

}