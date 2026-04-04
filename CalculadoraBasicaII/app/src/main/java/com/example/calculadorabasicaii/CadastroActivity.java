package com.example.calculadorabasicaii;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        Button btnCriaCadastro = findViewById(R.id.btnCriaCadastro);
        TextView btnVoltarLogin = findViewById(R.id.btnVoltarLogin);
        Intent telaLogin = new Intent(CadastroActivity.this, LoginActivity.class);

        btnCriaCadastro.setOnClickListener(v -> {
            //Criar lógica para cadastrar usuário no banco de dados.

            startActivity(telaLogin);
        });

        btnVoltarLogin.setOnClickListener(v -> {
            startActivity(telaLogin);
        });

    }
}