package com.example.calculadorabasicaii;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calculadorabasicaii.dao.UserDAO;

public class PerfilActivity extends AppCompatActivity {

    private Button btnSalvarPerfil;
    private EditText edtNomePerfil, edtNovaSenhaPerfil, edtConfirmaNovaSenha;
    private TextView btnVoltarMenu;
    private int userId;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        btnSalvarPerfil = findViewById(R.id.btnSalvarPerfil);
        edtNomePerfil = findViewById(R.id.edtNomePerfil);
        edtNovaSenhaPerfil = findViewById(R.id.edtNovaSenhaPerfil);
        edtConfirmaNovaSenha = findViewById(R.id.edtConfirmaNovaSenha);
        btnVoltarMenu = findViewById(R.id.btnVoltarMenu);

        userId = getIntent().getIntExtra("userId", -1);

        userDAO = new UserDAO(this);

        String userName = userDAO.getUserNameById(userId);
        edtNomePerfil.setText(userName);

        btnSalvarPerfil.setOnClickListener(v -> {
            String newName = edtNomePerfil.getText().toString().trim();
            String newPass = edtNovaSenhaPerfil.getText().toString();
            String confirmPass = edtConfirmaNovaSenha.getText().toString();
            boolean sucess;

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Senhas diferentes!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(newName.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                sucess = userDAO.updateUser(userId, newName, newPass);
            }

            if(!sucess){
                Toast.makeText(this, "Erro: Não foi possível atualizar o usuário!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Cadastro atualizado!", Toast.LENGTH_SHORT).show();
            navegarTelaMenu();
        });

        btnVoltarMenu.setOnClickListener(v -> finish());
    }

    private void navegarTelaMenu() {
        Intent telaMenu = new Intent(PerfilActivity.this, MenuActivity.class);
        telaMenu.putExtra("userId", userId);
        startActivity(telaMenu);
    }

}