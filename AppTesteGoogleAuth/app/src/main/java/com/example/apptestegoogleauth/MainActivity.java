package com.example.apptestegoogleauth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //private GoogleSignInClient googleClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        FirebaseUser logged_user = FirebaseAuth.getInstance().getCurrentUser();
/*
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN
                )
                        .requestEmail()
                        .build();

        googleClient = GoogleSignIn.getClient(this, signInOptions);

*/
        if(logged_user != null) {
            Log.i("CURRENTUSER", logged_user.getUid());
            startActivity(
                    new Intent(this, HomeActivity.class));
        } else {
            startActivity(
                    new Intent(this, LoginActivity.class));
        }

        finish();


    }
}