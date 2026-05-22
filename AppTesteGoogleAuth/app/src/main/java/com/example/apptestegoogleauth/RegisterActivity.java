package com.example.apptestegoogleauth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileReader;

public class RegisterActivity extends AppCompatActivity {

    private EditText editNameReg, editEmailReg, editPasswordReg;
    private TextView txtGoToLogin;
    private MaterialButton btnRegister;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        startComponents();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        boolean loginComGoogle = user.getProviderData().stream()
                .anyMatch(info -> info.getProviderId().equals("google.com"));
        if (loginComGoogle){
            txtGoToLogin.setVisibility(TextView.GONE);
            editPasswordReg.setVisibility(View.GONE);
            String nome = getIntent().getStringExtra("nome");
            String email = getIntent().getStringExtra("email");
            editNameReg.setText(nome);
            editEmailReg.setText(email);
        }

        btnRegister.setOnClickListener(v -> navegaTelaHome());

    }

    private void navegaTelaHome(){
        Intent telaHome = new Intent(this, HomeActivity.class);
        startActivity(telaHome);
    }

    private void startComponents(){
        editNameReg = findViewById(R.id.editNameReg);
        editEmailReg = findViewById(R.id.editEmailReg);
        editPasswordReg = findViewById(R.id.editPasswordReg);
        txtGoToLogin = findViewById(R.id.txtGoToLogin);
        btnRegister = findViewById(R.id.btnRegister);
    }
}