package com.example.weatherapp_p2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.exceptions.ClearCredentialException;

import com.example.weatherapp_p2.database.AppDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    private Button btnGoToFavorites, btnGoToProfile, btnUpdateCity;
    private EditText etCurrentCity;
    private ImageView ivLogout, ivWeatherIcon, ivFavorite;
    private TextView tvTemperature;
    private FirebaseAuth mAuth;
    private AppDatabase db;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private static final String API_KEY = BuildConfig.MINHA_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        startComponents();

        btnGoToProfile.setOnClickListener(v -> navegaTelaPerfil());

        btnGoToFavorites.setOnClickListener(v -> navegaTelaFavoritos());

        ivLogout.setOnClickListener(v -> navegaTelaLogin());

        btnUpdateCity.setOnClickListener(v -> {
            String cidade = etCurrentCity.getText().toString();
            if (!cidade.isEmpty()) {
                fetchWeatherData(cidade);
            } else {
                Toast.makeText(this, "Informe uma cidade!", Toast.LENGTH_SHORT).show();
            }
        });

        //fetchWeatherData("Indaiatuba");

    }

    private void startComponents(){
        btnGoToFavorites = findViewById(R.id.btnGoToFavorites);
        btnGoToProfile = findViewById(R.id.btnGoToProfile);
        btnUpdateCity = findViewById(R.id.btnUpdateCity);
        etCurrentCity = findViewById(R.id.etCurrentCity);
        ivLogout = findViewById(R.id.ivLogout);
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon);
        ivFavorite = findViewById(R.id.ivFavorite);
        tvTemperature = findViewById(R.id.tvTemperature);
        mAuth = FirebaseAuth.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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

    private void fetchWeatherData(String cidade){
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cidade + "&appid=" + API_KEY + "&units=metric";
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();

            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string(); //retorno json API
                runOnUiThread(() -> updateUI(result));
            } catch (IOException e) {
                Log.e("API OPENWEATHER", e.getMessage());
                Toast.makeText(this, "API access error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void fetchCityData(double lat, double lon, String cidade) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + API_KEY;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();

            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string(); //retorno json API
                etCurrentCity.setText(cidade);
                runOnUiThread(() -> updateUI(result));
            } catch (IOException e) {
                Log.e("API OPENWEATHER", e.getMessage());
                Toast.makeText(this, "API access error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void updateUI(String result) {

        if(result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject main  = jsonObject.getJSONObject("main");
                double temperature = main.getDouble("temp");

                //String description = jsonObject.getJSONArray("weather")
                //        .getJSONObject(0)
                //        .getString("description");

                String iconCode = jsonObject.getJSONArray("weather")
                        .getJSONObject(0).getString("icon");

                String resourceName = "ic_" + iconCode;
                int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
                ivWeatherIcon.setImageResource(resId);
                tvTemperature.setText(String.format("%.0f°", temperature));

            } catch (JSONException e) {
                Log.e("JSON API", e.getMessage());
                Toast.makeText(this, "API JSON error! " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void coordenadasLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // 2.1 Tente obter a última localização
              /*  fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        txtLocalizacao.setText("Lat: " + latitude + "\nLong: " + longitude);
                    } else {
                        // Se for null, avise o usuário para abrir o Maps e fechar, ou use getCurrentLocation
                        txtLocalizacao.setText("Cache de localização vazio. Abra o Google Maps para atualizar.");
                    }
                });*/
            // 2.2 Tente obter a localização atual
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            //etCurrentCity.setText("Lat: " + latitude + " Long: " + longitude);
                            fetchCityData(latitude, longitude, "local atual");
                        } else {
                            Toast.makeText(this,"Cache de localização vazio. Abra o Google Maps para atualizar.", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            // 3. Se não tem permissão é necessário solicitar
            obterLocalizacao();
        }
    }

    private void obterLocalizacao() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST
            );
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == LOCATION_PERMISSION_REQUEST) {

            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {

                obterLocalizacao();

            } else {
                Toast.makeText(this, "Permissão para obter localização foi negada.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onStart(){
        super.onStart();
        coordenadasLocalizacao();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (etCurrentCity.getText().toString().equals(getString(R.string.hint_city))) {
            coordenadasLocalizacao();
        }
    }

}