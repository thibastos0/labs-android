package com.example.calculadorabasicaii;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calculadorabasicaii.dao.UserDAO;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtSenha;
    private TextView txtCadastro;
    private Button btnLogin;
    private UserDAO userDAO;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        boolean logged = sharedPreferences.getBoolean("logged", false);

        if(logged) {
            int userId = sharedPreferences.getInt("userId", -1);
            navegarParaTelaMenu(userId);
            return;
        } else {
            setContentView(R.layout.activity_login);
        }

        btnLogin = findViewById(R.id.btnLoginAction);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        txtCadastro = findViewById(R.id.txtLinkTelaCadastro);

        userDAO = new UserDAO(this);

        btnLogin.setOnClickListener(v -> {
            //Criar lógica para logar usuário no banco de dados.
            String mail = edtEmail.getText().toString();
            String pass = edtSenha.getText().toString();

            int id = userDAO.login(mail, pass);

            if(id == -1) {
                Toast.makeText(this, "Login Inválido!", Toast.LENGTH_SHORT).show();
                return;
            }
            salvarSection(id);
            navegarParaTelaMenu(id);
        });

        txtCadastro.setOnClickListener(v -> {
            navegarParaTelaCadastro();
        });

    }

    private void navegarParaTelaMenu(int userId) {
        Intent telaMenu = new Intent(LoginActivity.this, MenuActivity.class);
        telaMenu.putExtra("userId", userId);
        startActivity(telaMenu);
    }

    private void salvarSection(int userId) {
        sharedPreferences.edit().putBoolean("logged", true).apply();
        sharedPreferences.edit().putInt("userId", userId).apply();
    }

    private void navegarParaTelaCadastro(){
        Intent telaCadastro = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(telaCadastro);
    }

}