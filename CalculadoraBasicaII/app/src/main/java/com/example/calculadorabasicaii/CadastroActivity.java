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
import com.example.calculadorabasicaii.model.User;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtNomeCad, edtEmailCad, edtSenhaCad, edtConfirmaSenhaCad;
    private Button btnCriaCadastro;
    private TextView txtVoltarLogin;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        userDAO = new UserDAO(this);

        edtNomeCad = findViewById(R.id.edtNomeCad);
        edtEmailCad = findViewById(R.id.edtEmailCad);
        edtSenhaCad = findViewById(R.id.edtSenhaCad);
        edtConfirmaSenhaCad = findViewById(R.id.edtConfirmaSenhaCad);
        btnCriaCadastro = findViewById(R.id.btnCriaCadastro);
        txtVoltarLogin = findViewById(R.id.txtVoltarLogin);

        btnCriaCadastro.setOnClickListener(v -> {
            //Lógica para cadastrar usuário no banco de dados.
            String name = edtNomeCad.getText().toString().trim();
            String mail = edtEmailCad.getText().toString().trim();
            String pass = edtSenhaCad.getText().toString();
            String confirmPass = edtConfirmaSenhaCad.getText().toString();
            boolean sucess = false;

            if (!pass.equals(confirmPass)) {
                Toast.makeText(this, "Senhas diferentes!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(name.isEmpty() || mail.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()){
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                User user = new User(name, mail, pass);
                sucess = userDAO.insertUser(user);
            }

            if(!sucess){
                Toast.makeText(this, "Erro: Usuaário já existe!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Cadastro realizado!", Toast.LENGTH_SHORT).show();
            finish();

        });

        txtVoltarLogin.setOnClickListener(v -> {
            navergarParaTelaLogin();
        });

    }

    private void navergarParaTelaLogin() {
        Intent telaLogin = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(telaLogin);
    }

}