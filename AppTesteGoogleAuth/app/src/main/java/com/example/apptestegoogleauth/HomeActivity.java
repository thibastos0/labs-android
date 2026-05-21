package com.example.apptestegoogleauth;

import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.exceptions.ClearCredentialException;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executors;


public class HomeActivity extends AppCompatActivity {

    private MaterialButton btnLogout;
    private TextView txtProfileName, txtProfileEmail;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);
        txtProfileName = findViewById(R.id.txtProfileName);
        txtProfileEmail = findViewById(R.id.txtProfileEmail);

        txtProfileEmail.setText(mAuth.getCurrentUser().getEmail());
        txtProfileName.setText(mAuth.getCurrentUser().getDisplayName());


        btnLogout.setOnClickListener(v -> signOut());
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // When a user signs out, clear the current user credential state from all credential providers.
        ClearCredentialStateRequest clearRequest = new ClearCredentialStateRequest();
        CredentialManager credentialManager = CredentialManager.create(this);
        credentialManager.clearCredentialStateAsync(
                clearRequest,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<>() {
                    @Override
                    public void onResult(@NonNull Void result) {
                        //updateUI(null);
                        navegaTelaLogin();
                    }

                    @Override
                    public void onError(@NonNull ClearCredentialException e) {
                        Log.e("GoogleAuth", "Não foi possível limpar as credenciais do usuário: " + e.getLocalizedMessage());

                    }
                });
    }

    private void navegaTelaLogin(){
        Intent telaLogin = new Intent(this, LoginActivity.class);
        startActivity(telaLogin);
    }

}