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

    // Referencias a la UI
    private EditText etLat, etLng;
    private TextView tvNearest, tvDist;
    private Button btnMyLocation, btnCalcular;

    // Helper para ubicación (permisos + FLP) y lista de centros
    private LocationHelper locationHelper;
    private List<ShoppingCenter> centers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);              // Habilita contenido de borde a borde
        setContentView(R.layout.activity_main);

        // Obtener vistas del layout
        etLat = findViewById(R.id.etLat);
        etLng = findViewById(R.id.etLng);
        tvNearest = findViewById(R.id.tvNearest);
        tvDist = findViewById(R.id.tvDist);
        btnMyLocation = findViewById(R.id.btnMyLocation);
        btnCalcular = findViewById(R.id.btnCalcular);

        // Cargar centros desde XML (parser propio)
        try {
            centers = CentersParser.parse(this);
        } catch (Exception e) {
            centers = Collections.emptyList();
            Toast.makeText(this, "Error leyendo XML de centros: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Inicializar helper de ubicación
        locationHelper = new LocationHelper(this);

        // Listeners de botones
        btnMyLocation.setOnClickListener(v -> useMyLocation());           // Rellena lat/lng con la última ubicación
        btnCalcular.setOnClickListener(v -> calculateNearestFromInputs()); // Usa los valores escritos para calcular

        // Solicitar permisos si faltan; si ya están, verificar/solicitar ajustes de ubicación (GPS, etc.)
        if (!locationHelper.hasLocationPermission()) {
            locationHelper.requestPermissions();
        } else {
            locationHelper.ensureSettingsOrResolve();
        }
    }

    // Botón "Usar mi ubicación": pide última ubicación y calcula el centro más cercano
    private void useMyLocation() {
        if (!locationHelper.hasLocationPermission()) {
            locationHelper.requestPermissions();
            return;
        }
        locationHelper.ensureSettingsOrResolve();
        locationHelper.getLastLocation(new LocationHelper.LastLocationCallback() {
            @Override
            public void onLocation(double lat, double lng) {
                // Pone la ubicación en los EditText y dispara el cálculo
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

    // Botón "Calcular": lee lat/lng del usuario, valida y calcula
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

    // Lógica central: recorre la lista y encuentra el ShoppingCenter más cercano a (lat, lng)
    private void calculateNearest(double lat, double lng) {
        if (centers == null || centers.isEmpty()) {
            tvNearest.setText("No hay centros cargados");
            tvDist.setText(getString(R.string.tv_dist_placeholder));
            return;
        }
        ShoppingCenter nearest = null;
        double bestKm = Double.MAX_VALUE;

        // Calcula distancia Haversine a cada centro y se queda con la menor
        for (ShoppingCenter c : centers) {
            double km = DistanceUtil.haversineKm(lat, lng, c.lat, c.lng);
            if (km < bestKm) {
                bestKm = km;
                nearest = c;
            }
        }

        // Muestra resultado si encontró alguno
        if (nearest != null) {
            tvNearest.setText(nearest.nombre + " — " + nearest.provincia);
            tvDist.setText(String.format("Distancia: %.2f km", bestKm));
        }
    }

    // Callback de permisos en tiempo de ejecución
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
                // Si concedieron, volvemos a chequear/solicitar ajustes de ubicación
                locationHelper.ensureSettingsOrResolve();
                Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sin permisos de ubicación", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Resultado del diálogo del sistema que intenta resolver ajustes de ubicación (GPS, etc.)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // No hay manejo adicional aquí; si el usuario activó la ubicación, próximos intentos funcionarán.
    }
}
