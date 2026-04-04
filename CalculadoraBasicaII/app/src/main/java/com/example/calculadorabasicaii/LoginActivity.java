package com.example.calculadorabasicaii;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLoginAction);
        TextView txtCadastro = findViewById(R.id.txtLinkTelaCadastro);

        btnLogin.setOnClickListener(v -> {
            //Criar lógica para logar usuário no banco de dados.
            Intent telaMenu = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(telaMenu);
        });

        txtCadastro.setOnClickListener(v -> {
            Intent telaCadastro = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(telaCadastro);
        });

    }
}