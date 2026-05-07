package com.example.provaandroid_firebase;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import android.text.InputType;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText et_password, et_password_confirm;
    private ImageButton iv_toggle_password, iv_toggle_password_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        et_password = findViewById(R.id.et_password);
        et_password_confirm = findViewById(R.id.et_password_confirm);
        iv_toggle_password = findViewById(R.id.iv_toggle_password);
        iv_toggle_password_confirm = findViewById(R.id.iv_toggle_password_confirm);
        toolbar = findViewById(R.id.toolbar_register);

        // Toggle de visibilidade da senha
        iv_toggle_password.setOnClickListener(v -> toggleSenha(et_password, iv_toggle_password));
        iv_toggle_password_confirm.setOnClickListener(v -> toggleSenha(et_password_confirm, iv_toggle_password_confirm));

        // Registra como a ActionBar desta Activity
        setSupportActionBar(toolbar);

        // Ativa a seta de voltar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // Captura o clique na seta
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // fecha esta Activity e volta para a anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
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