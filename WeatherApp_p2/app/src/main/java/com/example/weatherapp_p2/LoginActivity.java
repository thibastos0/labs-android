package com.example.weatherapp_p2;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton btnGoogleLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        startComponents();

        btnGoogleLogin.setOnClickListener(v -> signInGoogle());

    }
/*
    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            navegaTelaHome();
        }
    }*/

    private void startComponents(){

        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        mAuth = FirebaseAuth.getInstance();
    }

    private void signInGoogle(){

        // Instantiate a Google sign-in request
        // defaul_web_client_id só aparece se der build já com o novo google-services.json
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.default_web_client_id))
                .build();

        // Create the Credential Manager request
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        CredentialManager credentialManager = CredentialManager.create(this);
        //referênica: https://github.com/firebase/snippets-android
        // caminho: auth/app/src/main/java/com/google/firebase/quickstart/auth/GoogleSignInActivity.java
        credentialManager.getCredentialAsync(
                this,
                request,
                null, // CancellationSignal, pode deixar null por enquanto
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        //Pega a credencial e chama signin
                        handleSignIn(result.getCredential());
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        Log.e("GoogleAuth", "Erro ao obter credencial. e: " + e.getLocalizedMessage());
                    }
                }
        );

    }

    private void handleSignIn(Credential credential) {
        // Check if credential is of type Google ID
        if (credential instanceof CustomCredential customCredential
                && credential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            // Create Google ID Token
            Bundle credentialData = customCredential.getData();
            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
        } else {
            Log.w("GoogleAuth", "Credencial não é do tipo Google ID!");
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("GoogleAuth", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        boolean isNewUser = task.getResult()
                                .getAdditionalUserInfo()
                                .isNewUser();

                        if (isNewUser) {
                            Intent navegaTelaProfile = new Intent(this, ProfileActivity.class);
                            navegaTelaProfile.putExtra("nome", user.getDisplayName());
                            navegaTelaProfile.putExtra("email", user.getEmail());
                            navegaTelaProfile.putExtra("isNewUser", isNewUser);
                            startActivity(navegaTelaProfile);
                        } else {
                            navegaTelaHome();
                        }
                    } else {
                        // If sign in fails, display a message to the user
                        Log.w("GoogleAuth", "signInWithCredential:failure ", task.getException());
                    }
                });
    }

    private void navegaTelaHome(){
        Intent telaHome = new Intent(this, HomeActivity.class);
        startActivity(telaHome);
    }

}