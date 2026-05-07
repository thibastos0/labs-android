package com.example.provaandroid_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextView tv_go_to_register;
    private EditText et_login_password;
    private ImageButton ivTogglePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        tv_go_to_register = findViewById(R.id.tv_go_to_register);
        ivTogglePassword = findViewById(R.id.iv_toggle_login_password);
        et_login_password = findViewById(R.id.et_login_password);

        // Toggle de visibilidade da senha
        ivTogglePassword.setOnClickListener(v -> toggleSenha(et_login_password, ivTogglePassword));

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

}