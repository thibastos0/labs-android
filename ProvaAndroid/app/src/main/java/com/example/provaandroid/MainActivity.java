package com.example.provaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnIrCalculoMedia, btnIrCadastroAluno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnIrCalculoMedia = findViewById(R.id.btnIrCalculoMedia);
        btnIrCadastroAluno = findViewById(R.id.btnIrCadastroAluno);


        btnIrCalculoMedia.setOnClickListener(view -> {
            irCalculoMedia();
        });
        btnIrCadastroAluno.setOnClickListener(view -> {
            irCadastroAluno();
        });

    }

    private void irCalculoMedia() {
        Intent telaCalculoNota = new Intent(MainActivity.this, CaluloNotaActivity.class);
        startActivity(telaCalculoNota);
    }

    private void irCadastroAluno() {
        Intent telaCadastroAluno = new Intent(MainActivity.this, CadastroActivity.class);
        startActivity(telaCadastroAluno);
    }

}