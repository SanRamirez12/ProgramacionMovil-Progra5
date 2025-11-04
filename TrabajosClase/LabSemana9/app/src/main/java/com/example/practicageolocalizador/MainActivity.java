package com.example.practicageolocalizador;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etLat, etLng;
    private TextView tvNearest, tvDist;
    private Button btnMyLocation, btnCalcular;

    private LocationHelper locationHelper;
    private List<ShoppingCenter> centers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Views
        etLat = findViewById(R.id.etLat);
        etLng = findViewById(R.id.etLng);
        tvNearest = findViewById(R.id.tvNearest);
        tvDist = findViewById(R.id.tvDist);
        btnMyLocation = findViewById(R.id.btnMyLocation);
        btnCalcular = findViewById(R.id.btnCalcular);

        // Cargar centros
        try {
            centers = CentersParser.parse(this);
        } catch (Exception e) {
            centers = Collections.emptyList();
            Toast.makeText(this, "Error leyendo XML de centros: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Helper de ubicación
        locationHelper = new LocationHelper(this);

        btnMyLocation.setOnClickListener(v -> useMyLocation());
        btnCalcular.setOnClickListener(v -> calculateNearestFromInputs());

        // Permisos al iniciar
        if (!locationHelper.hasLocationPermission()) {
            locationHelper.requestPermissions();
        } else {
            locationHelper.ensureSettingsOrResolve();
        }
    }

    private void useMyLocation() {
        if (!locationHelper.hasLocationPermission()) {
            locationHelper.requestPermissions();
            return;
        }
        locationHelper.ensureSettingsOrResolve();
        locationHelper.getLastLocation(new LocationHelper.LastLocationCallback() {
            @Override
            public void onLocation(double lat, double lng) {
                etLat.setText(String.valueOf(lat));
                etLng.setText(String.valueOf(lng));
                calculateNearest(lat, lng);
            }
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateNearestFromInputs() {
        String sLat = etLat.getText() != null ? etLat.getText().toString().trim() : "";
        String sLng = etLng.getText() != null ? etLng.getText().toString().trim() : "";
        if (sLat.isEmpty() || sLng.isEmpty()) {
            Toast.makeText(this, "Ingresa latitud y longitud o usa mi ubicación", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            double lat = Double.parseDouble(sLat);
            double lng = Double.parseDouble(sLng);
            calculateNearest(lat, lng);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Formato de coordenadas inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateNearest(double lat, double lng) {
        if (centers == null || centers.isEmpty()) {
            tvNearest.setText("No hay centros cargados");
            tvDist.setText(getString(R.string.tv_dist_placeholder));
            return;
        }
        ShoppingCenter nearest = null;
        double bestKm = Double.MAX_VALUE;

        for (ShoppingCenter c : centers) {
            double km = DistanceUtil.haversineKm(lat, lng, c.lat, c.lng);
            if (km < bestKm) {
                bestKm = km;
                nearest = c;
            }
        }
        if (nearest != null) {
            tvNearest.setText(nearest.nombre + " — " + nearest.provincia);
            tvDist.setText(String.format("Distancia: %.2f km", bestKm));
        }
    }

    // Permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LocationHelper.REQ_PERMISSIONS) {
            boolean granted = false;
            for (int r : grantResults) {
                if (r == PackageManager.PERMISSION_GRANTED) { granted = true; break; }
            }
            if (granted) {
                locationHelper.ensureSettingsOrResolve();
                Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sin permisos de ubicación", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Resolución de ajustes de ubicación (si el sistema abre un diálogo)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // No es necesario manejar nada específico; si el usuario activó ubicación,
        // los siguientes intentos funcionarán.
    }
}
