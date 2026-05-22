package com.example.apptestegoogleauth;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.button.MaterialButton;

public class GeolocActivity extends AppCompatActivity {

    private MaterialButton btnLocalizacao;
    private TextView txtLocalizacao;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_geoloc);

        startComponents();

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        btnLocalizacao.setOnClickListener(v -> {
            // 1. Verifique a permissão antes de tentar pegar a localização
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
                                txtLocalizacao.setText("Lat: " + latitude + "\nLong: " + longitude);
                            } else {
                                txtLocalizacao.setText("Cache de localização vazio. Abra o Google Maps para atualizar.");
                            }
                        });

            } else {
                // 3. Se não tem permissão, peça-a
                obterLocalizacao();
            }
        });



    }

    private void startComponents() {
        txtLocalizacao = findViewById(R.id.txtLocalizacao);
        btnLocalizacao = findViewById(R.id.btnLocalizacao);
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

                txtLocalizacao.setText(
                        "Permissão negada"
                );
            }
        }
    }

}