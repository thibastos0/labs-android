package com.example.calculadorabasicaii;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        Button btnIrCalculadora = findViewById(R.id.btnIrCalculadora);
        Button btnSair = findViewById(R.id.btnSair);

        btnIrCalculadora.setOnClickListener(v -> {
            Intent telaCalculadora = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(telaCalculadora);
        });

        btnSair.setOnClickListener(v -> {
            //logica para deslogar usuário.
            Intent telaLogin = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(telaLogin);
        });
    }
}