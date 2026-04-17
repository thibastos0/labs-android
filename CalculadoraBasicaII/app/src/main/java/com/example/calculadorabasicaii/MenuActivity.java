package com.example.calculadorabasicaii;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calculadorabasicaii.dao.UserDAO;

public class MenuActivity extends AppCompatActivity {

    private int userId;
    TextView txtSalute;
    Button btnIrCalculadora, btnPerfil, btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        btnIrCalculadora = findViewById(R.id.btnIrCalculadora);
        btnPerfil = findViewById(R.id.btnPerfil);
        btnSair = findViewById(R.id.btnSair);
        txtSalute = findViewById(R.id.txtSalute);
        userId = getIntent().getIntExtra("userId", -1);

        UserDAO userDAO = new UserDAO(this);
        String userName = userDAO.getUserNameById(userId);

        txtSalute.setText("Olá, " + userName + "!");

        btnIrCalculadora.setOnClickListener(v -> {
            navegarParaTelaCalculadora(userId);
        });

        btnPerfil.setOnClickListener(v -> {
            navegarParaTelaPerfil(userId);
        });

        btnSair.setOnClickListener(v -> {
            //logica para deslogar usuário?
            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            navegarParaTelaLogin();
        });
    }

    private void navegarParaTelaLogin() {
        Intent telaLogin = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(telaLogin);
    }

    private void navegarParaTelaCalculadora(int userId){
        Intent telaCalculadora = new Intent(MenuActivity.this, MainActivity.class);
        telaCalculadora.putExtra("userId", userId);
        startActivity(telaCalculadora);
    }

    private void navegarParaTelaPerfil(int userId){
        Intent telaPerfil = new Intent(MenuActivity.this, PerfilActivity.class);
        telaPerfil.putExtra("userId", userId);
        startActivity(telaPerfil);
    }

}