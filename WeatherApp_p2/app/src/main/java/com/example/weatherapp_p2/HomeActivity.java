package com.example.weatherapp_p2;

import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.exceptions.ClearCredentialException;

import com.example.weatherapp_p2.database.AppDatabase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    private Button btnGoToFavorites, btnGoToProfile;
    private ImageView ivLogout;
    private FirebaseAuth mAuth;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        startComponents();

        btnGoToProfile.setOnClickListener(v -> navegaTelaPerfil());

        btnGoToFavorites.setOnClickListener(v -> navegaTelaFavoritos());

        ivLogout.setOnClickListener(v -> navegaTelaLogin());

    }

    private void startComponents(){
        btnGoToFavorites = findViewById(R.id.btnGoToFavorites);
        btnGoToProfile = findViewById(R.id.btnGoToProfile);
        ivLogout = findViewById(R.id.ivLogout);
        mAuth = FirebaseAuth.getInstance();
    }

    private void navegaTelaFavoritos(){
        Intent telaFavoritos = new Intent(this, FavoritesActivity.class);
        startActivity(telaFavoritos);
    }

    private void navegaTelaPerfil(){
        Intent telaPerfil = new Intent(this, ProfileActivity.class);
        telaPerfil.putExtra("isNewUser", false);
        startActivity(telaPerfil);
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
        telaLogin.addFlags(telaLogin.FLAG_ACTIVITY_NEW_TASK | telaLogin.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(telaLogin);
        finish();
    }

}