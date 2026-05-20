package com.example.provaandroid_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.*;


public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private LinearLayout btn_google_login;
    //private SignInButton btn_google_login;
    private TextView tv_go_to_register;
    private EditText et_login_password, et_login_email;
    private ImageButton ivTogglePassword;
    private FirebaseAuth aluno = FirebaseAuth.getInstance();
    private GoogleSignInClient googleClient;
    private final ActivityResultLauncher<Intent> googleLauncher =
            registerForActivityResult(
                    new ActivityResultContracts
                            .StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            try {
                                Task<GoogleSignInAccount> task =
                                        GoogleSignIn.getSignedInAccountFromIntent(
                                                result.getData()
                                        );
                                GoogleSignInAccount account =
                                        task.getResult(ApiException.class);

                                firebaseAuthWithGoogle(account.getIdToken());

                            } catch (ApiException e) {
                                // Erros específicos do Google (ex: 12500, 10, 7)
                                Log.e("GoogleAuth", "Erro API: " + e.getStatusCode());
                                Toast.makeText(this, "Erro Google: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                // Outros erros inesperados
                                Log.e("GoogleAuth", "Erro inesperado: " + e.getMessage());
                                Toast.makeText(this, "Falha no login Google.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // O usuário cancelou ou fechou a tela de seleção de conta
                            Log.w("GoogleAuth", "Login cancelado pelo usuário.");
                        }
                    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_google_login = findViewById(R.id.btn_google_login);
        tv_go_to_register = findViewById(R.id.tv_go_to_register);
        ivTogglePassword = findViewById(R.id.iv_toggle_login_password);
        et_login_password = findViewById(R.id.et_login_password);
        et_login_email = findViewById(R.id.et_login_email);



        // Toggle de visibilidade da senha
        ivTogglePassword.setOnClickListener(v -> toggleSenha(et_login_password, ivTogglePassword));

        btn_login.setOnClickListener(v -> {
            login(v);
        });

        //Autenticação Google
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken(
                getString(R.string.default_web_client_id)
                )
                .requestEmail()
                .build();

        googleClient = GoogleSignIn.getClient(this, googleSignInOptions);

        btn_google_login.setOnClickListener(v -> signInGoogle());

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
    private void login(View v){
        String email = et_login_email.getText().toString();
        String password = et_login_password.getText().toString();

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        aluno.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            String erro;
                            try {
                                throw  task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                erro = "Email não existe ou a conta foi desativada.";
                            } catch (FirebaseNetworkException e) {
                                erro = "Dispositivo offline.";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "Senha incorreta ou formato do e-mail inválido";
                            } catch (Exception e) {
                                erro = "Erro ao tentar fazer o login do usuário!";
                            }
                            Toast.makeText(LoginActivity.this, erro, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        navegaTelaMain();
                        finish();

                    }
                });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        aluno.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login bem-sucedido
                        Log.d("GoogleAuth", "singInWithCredencial:success");
                        //FirebaseUser user = aluno.getCurrentUser();
                        navegaTelaMain();
                        finish();
                    } else {
                        // Falha no login
                        Log.w("GoogleAuth", "signInWithCredencial:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Erro na autenticação com Google.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInGoogle() {
        Intent signInIntent = googleClient.getSignInIntent();
        googleLauncher.launch(signInIntent);
    }

}