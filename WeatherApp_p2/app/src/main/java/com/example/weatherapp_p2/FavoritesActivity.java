package com.example.weatherapp_p2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp_p2.dao.SavedPlaceDAO;
import com.example.weatherapp_p2.database.AppDatabase;
import com.example.weatherapp_p2.model.SavedPlace;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class FavoritesActivity extends AppCompatActivity {

    ListView lvFavorites;
    private AppDatabase db;
    private SavedPlace savedPlace;
    private FirebaseAuth mAuth;
    private List<SavedPlace> savedPlaceList = new ArrayList<>();
    private List<String> cidades = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorites);

        db = AppDatabase.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        lvFavorites = findViewById(R.id.lvFavorites);

        savedPlace = new SavedPlace();

        SavedPlaceDAO savedPlaceDAO = db.savedPlaceDAO();

        if (user == null) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
        try {

            savedPlaceList.clear();
            cidades.clear();

            savedPlaceList.addAll(savedPlaceDAO.loadAllById(user.getUid()));

            if(savedPlaceList.isEmpty()) { return; }

            for (SavedPlace place : savedPlaceList) {
                cidades.add(place.getCity() + ", " + place.getCountry());
            }

            // TUDO QUE MEXE NA INTERFACE (UI) DEVE ESTAR DENTRO DO runOnUiThread
            runOnUiThread(() -> {

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        FavoritesActivity.this,
                        android.R.layout.simple_list_item_1,
                        cidades
                );

                //carrega os itens na lista usando o Adapter
                lvFavorites.setAdapter(adapter);

                //configura click para cada item
                lvFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        SavedPlace itemSelecionado = savedPlaceList.get(position);
                        String cidadeSelecionada = cidades.get(position);

                        double latitude = itemSelecionado.getLatitude();
                        double longitude = itemSelecionado.getLongitude();

                        Intent telaHome = new Intent(FavoritesActivity.this, HomeActivity.class);
                        telaHome.putExtra("SELECTED_CITY", cidadeSelecionada);
                        telaHome.putExtra("SELECTED_LAT", latitude);
                        telaHome.putExtra("SELECTED_LON", longitude);

                        // Limpa o empilhamento de telas para não poluir o fluxo do app
                        telaHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        startActivity(telaHome);
                        finish();
                    }
                });

                Toast.makeText(FavoritesActivity.this, "Lista carregada com sucesso!", Toast.LENGTH_SHORT).show();
            });

        } catch (Exception e) {
            runOnUiThread(() -> {
                Toast.makeText(FavoritesActivity.this, "Erro ao carregar lista de cidades: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        }
    });

    }


}