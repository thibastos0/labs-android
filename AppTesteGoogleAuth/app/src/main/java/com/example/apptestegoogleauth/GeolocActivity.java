package com.example.apptestegoogleauth;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.button.MaterialButton;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class GeolocActivity extends AppCompatActivity {

    private MaterialButton btnLocalizacao;
    private TextView txtLocalizacao;
    private FusedLocationProviderClient fusedLocationClient;
    private MapView map;
    private Marker marker;
    private LocationCallback locationCallback;
    private static final int LOCATION_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_geoloc);

        startComponents();
        startMap();

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
                /*fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                txtLocalizacao.setText("Lat: " + latitude + "\nLong: " + longitude);
                            } else {
                                txtLocalizacao.setText("Cache de localização vazio. Abra o Google Maps para atualizar.");
                            }
                        });*/
                //2.3 Atualização em tempo real
                iniciarAtualizacaoLocalizacao();

            } else {
                // 3. Se não tem permissão, peça-a
                pedirPermissao();
            }
        });



    }

    private void startComponents() {
        txtLocalizacao = findViewById(R.id.txtLocalizacao);
        btnLocalizacao = findViewById(R.id.btnLocalizacao);
        map = findViewById(R.id.map);
    }

    private void startMap(){
        // Configuração do OSM
        // Configura o User Agent do OpenStreetMap
        // Necessário para evitar bloqueios do serviço
        Configuration.getInstance().setUserAgentValue(getPackageName());

        // Tipo de mapa
        map.setTileSource(TileSourceFactory.MAPNIK);

        // Zoom multitouch
        map.setMultiTouchControls(true);

        // Coordenadas
        GeoPoint startPoint =
                new GeoPoint(-23.1217446, -47.255441);

        // Zoom
        map.getController().setZoom(15.0);

        // Centralizar mapa
        map.getController().setCenter(startPoint);

        // Marcador
        marker = new Marker(map);
        marker.setPosition(startPoint);
        marker.setTitle("São Paulo");

        map.getOverlays().add(marker);
    }

    private void pedirPermissao() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST
            );
        } else {
            iniciarAtualizacaoLocalizacao();
        }
    }

    //Inicia rastreamento em tempo real
    private void iniciarAtualizacaoLocalizacao(){

        // Configuração das atualizações
        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,3000) // 3 segundos
                .setMinUpdateDistanceMeters(5) // 5 metros
                .build();

        // Callback chamado quando localização muda
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult == null) {
                    return;
                }

                // Pega última localização
                Location location = locationResult.getLastLocation();

                if (location != null) {

                    // Latitude
                    double latitude = location.getLatitude();
                    // Longitude
                    double longitude = location.getLongitude();

                    // Cria ponto geográfico
                    GeoPoint pontoAtual = new GeoPoint(latitude, longitude);

                    // Move mapa até localização
                    map.getController()
                            .animateTo(pontoAtual);

                    // Define zoom
                    map.getController()
                            .setZoom(18.0);

                    // Atualiza posição do marcador
                    marker.setPosition(pontoAtual);

                    // Texto do marcador
                    marker.setTitle("Estou aqui");

                    // Adiciona marcador apenas uma vez
                    if (!map.getOverlays().contains(marker)) {
                        map.getOverlays().add(marker);
                    }

                    // Atualiza mapa
                    map.invalidate();
                }
            }
        };

        // Verifica permissão novamente
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Inicia atualizações do GPS
        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                getMainLooper());

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

                iniciarAtualizacaoLocalizacao();

            } else {

                txtLocalizacao.setText(
                        "Permissão negada"
                );
            }
        }
    }

    // Retoma mapa
    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    // Pausa mapa
    @Override
    protected void onPause() {
        super.onPause();

        map.onPause();

        // Para atualizações GPS para economizar bateria
        if (fusedLocationClient != null
                && locationCallback != null) {

            fusedLocationClient.removeLocationUpdates(
                    locationCallback);
        }
    }

}