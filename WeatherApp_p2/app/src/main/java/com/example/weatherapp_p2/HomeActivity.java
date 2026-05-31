package com.example.weatherapp_p2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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
import androidx.core.content.ContextCompat;
import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.exceptions.ClearCredentialException;

import com.example.weatherapp_p2.database.AppDatabase;
import com.example.weatherapp_p2.model.SavedPlace;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    private Button btnGoToFavorites, btnGoToProfile, btnUpdateCity;
    private EditText etCurrentCity;
    private ImageView ivLogout, ivWeatherIcon, ivFavorite;
    private TextView tvTemperature, tvDescription;
    private Map<String, Integer> iconMap = new HashMap<>();
    private FirebaseAuth mAuth;
    private AppDatabase db;
    private SavedPlace savedPlace;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private static final String API_KEY = BuildConfig.MINHA_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        db = AppDatabase.getInstance(this);
        savedPlace = new SavedPlace();
        startComponents();
        starIconMap();
        verificaFavorito();

        btnGoToProfile.setOnClickListener(v -> navegaTelaPerfil());

        btnGoToFavorites.setOnClickListener(v -> navegaTelaFavoritos());

        ivLogout.setOnClickListener(v -> navegaTelaLogin());

        ivFavorite.setOnClickListener(v -> salvaFavorito());

        btnUpdateCity.setOnClickListener(v -> {
            String cidade = etCurrentCity.getText().toString().trim();
            if (!cidade.isEmpty()) {
                fetchWeatherData(0,0, cidade);
            } else {
                Toast.makeText(this, "Informe uma cidade!", Toast.LENGTH_SHORT).show();
            }
        });

        //fetchWeatherData("Indaiatuba");

    }

    private void starIconMap() {
        iconMap.put("01d", R.drawable.ic_01d);
        iconMap.put("01n", R.drawable.ic_01n);
        iconMap.put("02d", R.drawable.ic_02d);
        iconMap.put("02n", R.drawable.ic_02n);
        iconMap.put("03d", R.drawable.ic_03d);
        iconMap.put("03n", R.drawable.ic_03n);
        iconMap.put("04d", R.drawable.ic_04d);
        iconMap.put("04n", R.drawable.ic_04n);
        iconMap.put("09d", R.drawable.ic_09d);
        iconMap.put("09n", R.drawable.ic_09n);
        iconMap.put("10d", R.drawable.ic_10d);
        iconMap.put("10n", R.drawable.ic_10n);
        iconMap.put("11d", R.drawable.ic_11d);
        iconMap.put("11n", R.drawable.ic_11n);
        iconMap.put("13d", R.drawable.ic_13d);
        iconMap.put("13n", R.drawable.ic_13n);
        iconMap.put("50d", R.drawable.ic_50d);
        iconMap.put("50n", R.drawable.ic_50n);
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
        tvDescription = findViewById(R.id.tvDescription);
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

    private void verificaFavorito() {

        String userId = mAuth.getCurrentUser().getUid();
        String cidadeAtual = etCurrentCity.getText().toString().trim();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                //verifica se existe a Cidade para o usuário
                int count = db.savedPlaceDAO().isSavedPlace(userId, cidadeAtual);

                runOnUiThread(() -> {
                    if (count > 0) {
                        ivFavorite.setTag(R.drawable.ic_heart_filled);
                        ivFavorite.setImageResource(R.drawable.ic_heart_filled);
                        Toast.makeText(this, "Cidade está entre os favoritos.", Toast.LENGTH_SHORT).show();
                    } else {
                        ivFavorite.setTag(R.drawable.ic_heart_border);
                        ivFavorite.setImageResource(R.drawable.ic_heart_border);
                        Toast.makeText(this, "Clique no coração para favoritar a cidade.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                // Se algo deu errado
                runOnUiThread(() -> {
                    Log.e("VerificaFavorito", e.getMessage());
                    Toast.makeText(this, "Erro ao verificar se é favorito: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }
    private void salvaFavorito() {

        Integer resourceId = (Integer) ivFavorite.getTag();

        if (resourceId == R.drawable.ic_heart_border) {

            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    //salva cidade no BD
                    db.savedPlaceDAO().insterSavedPlace(savedPlace);

                    runOnUiThread(() -> {
                        ivFavorite.setImageResource(R.drawable.ic_heart_filled);
                        ivFavorite.setTag(R.drawable.ic_heart_filled);
                        Toast.makeText(this, "Cidade adicionada aos favoritos!", Toast.LENGTH_SHORT).show();
                    });
                } catch (Exception e) {
                    // Se algo deu errado
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Erro ao adicionar como favorito: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });


        } else {

            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    //deleta cidade no BD
                    db.savedPlaceDAO().deleteSavedPlace(savedPlace);

                    runOnUiThread(() -> {
                        ivFavorite.setImageResource(R.drawable.ic_heart_border);
                        ivFavorite.setTag(R.drawable.ic_heart_border);
                        Toast.makeText(this, "Cidade removida dos favoritos!", Toast.LENGTH_SHORT).show();
                    });
                } catch (Exception e) {
                    // Se algo deu errado
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Erro ao remover como favorito: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });

        }

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

    private void fetchWeatherData(double lat, double lon, String cidade) {

        String url_1 = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&lang=pt_br&appid=" + API_KEY;
        String url_2 = "https://api.openweathermap.org/data/2.5/weather?q=" + cidade + "&units=metric&lang=pt_br&appid=" + API_KEY;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request;

            if (cidade.equals("CurrentLocation")) {
                request = new Request.Builder().url(url_1).build();
            } else {
                request = new Request.Builder().url(url_2).build();
            }

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body().string(); //retorno json API
                    runOnUiThread(() -> updateUI(result));
                } else {
                    Log.e("API OPENWEATHER", "Erro no request (comunicação)" + response.code());
                }
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

                String nomeCidade = jsonObject.getString("name");
                double temperature = main.getDouble("temp");
                String iconCode = jsonObject.getJSONArray("weather")
                        .getJSONObject(0).getString("icon");
                String descricao = jsonObject.getJSONArray("weather")
                        .getJSONObject(0).getString("description");

                String country = jsonObject.getJSONObject("sys")
                        .getString("country");
                double latitude = jsonObject.getJSONObject("coord")
                        .getDouble("lat");
                double longitude = jsonObject.getJSONObject("coord")
                        .getDouble("lon");
                long cityId = jsonObject.getLong("id");

                savedPlace.setUserId(mAuth.getCurrentUser().getUid());
                savedPlace.setCity(nomeCidade);
                savedPlace.setCityId(cityId);
                savedPlace.setCountry(country);
                savedPlace.setLatitude(latitude);
                savedPlace.setLongitude(longitude);

                //String resourceName = "ic_" + iconCode;
                //int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
                ivWeatherIcon.setImageResource(iconMap.get(iconCode));
                tvTemperature.setText(String.format("%.0f°", temperature) + "C");
                tvDescription.setText(descricao);
                etCurrentCity.setText(nomeCidade);
                verificaFavorito();

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
                            Log.i("CurrentLocation", "Lat: " + latitude + " Long: " + longitude);
                            fetchWeatherData(latitude, longitude, "CurrentLocation");
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
    protected void onResume(){
        super.onResume();
        String cidadeAtual = etCurrentCity.getText().toString().trim();
        if (cidadeAtual.isEmpty()) {
            coordenadasLocalizacao();
        } else {
            //atualizar informações da cidade que estava selecionada no onResume()
            fetchWeatherData(0,0, cidadeAtual);
        }
    }

}