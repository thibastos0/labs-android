package com.example.provaandroid_firebase;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GradesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grades);


        toolbar = findViewById(R.id.toolbar_grades);

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

}
