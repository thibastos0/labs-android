package com.example.weatherapp_p2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp_p2.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail;
    private Button btnSaveProfile, btnCancel;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        startComponents();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        boolean isNewUser = getIntent().getBooleanExtra("isNewUser", false);

        boolean loginComGoogle = firebaseUser.getProviderData().stream()
                .anyMatch(info -> info.getProviderId().equals("google.com"));

        if (loginComGoogle && isNewUser){
            btnCancel.setVisibility(View.GONE);
            String nome = getIntent().getStringExtra("nome");
            String email = getIntent().getStringExtra("email");
            etName.setText(nome);
            etEmail.setText(email);
        }

        if (!isNewUser) {
            LoadData(firebaseUser);
        }

        btnSaveProfile.setOnClickListener(v -> {
            String uid = firebaseUser.getUid();
            String nome = etName.getText().toString();
            String email = etEmail.getText().toString();

            if(nome.isEmpty() || email.isEmpty()){
                Toast.makeText(this, "Necessário preencher todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }
            User userData = new User(uid, nome, email);
            saveProfile(userData);
        });

        btnCancel.setOnClickListener(v -> navegaTelaHome());

    }

    private void startComponents(){
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnCancel = findViewById(R.id.btnCancel);

        mAuth = FirebaseAuth.getInstance();
    }

    private void saveProfile(User userData) {

        DatabaseReference user = reference.child("users");

        user.child(userData.getUid()).setValue(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Sucesso ao gravar dados!", Toast.LENGTH_SHORT).show();
                navegaTelaHome();
            } else {
                Exception e = task.getException();
                Toast.makeText(this, "Erro ao gravar dados! " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void LoadData(FirebaseUser firebaseUser) {

        DatabaseReference user = reference.child("users");
        user.child(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Erro ao ler dados!", task.getException());
                    return;
                }
                else {
                    User userData = task.getResult().getValue(User.class);

                    if (userData != null) {
                        etName.setText(userData.getName());
                        etEmail.setText(userData.getMail());
                    }
                }
            }
        });
    }

    private void navegaTelaHome(){
        Intent telaHome = new Intent(this, HomeActivity.class);
        startActivity(telaHome);
    }

}