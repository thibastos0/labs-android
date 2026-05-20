package com.example.provaandroid_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        FirebaseUser logged_user = FirebaseAuth.getInstance().getCurrentUser();

        if(logged_user != null) {
            Log.i("CURRENTUSER", logged_user.getUid());
            startActivity(
                    new Intent(this, MainActivity.class));
        } else {
            startActivity(
                    new Intent(this, LoginActivity.class));
        }

        finish();

    }
}