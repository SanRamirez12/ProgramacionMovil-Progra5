package com.example.practicageolocalizador;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;

public class LocationHelper {

    // Códigos para onRequestPermissionsResult y startResolutionForResult
    public static final int REQ_PERMISSIONS = 2001;
    public static final int REQ_RESOLVE_SETTINGS = 2002;

    // Referencia a la Activity que usa este helper (para pedir permisos / resolver ajustes)
    private final Activity activity;
    // Cliente del Fused Location Provider (Google Play Services)
    private final FusedLocationProviderClient fused;
    // Configuración básica de solicitudes de ubicación (intervalo y prioridad)
    private final LocationRequest request;

    // Callback simple para devolver una última ubicación o un error de forma asíncrona
    public interface LastLocationCallback {
        void onLocation(double lat, double lng);
        void onError(String message);
    }

    public LocationHelper(Activity activity) {
        this.activity = activity;
        // Inicializa el cliente FLP
        this.fused = LocationServices.getFusedLocationProviderClient(activity);
        // PRIORITY_BALANCED_POWER_ACCURACY: buen balance entre precisión y consumo de batería
        // Intervalo "deseado" de 5s y mínimo de 2s (no garantiza updates; se usa para validar settings)
        this.request = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 5000L)
                .setMinUpdateIntervalMillis(2000L)
                .setWaitForAccurateLocation(false)
                .build();
    }

    // Verifica si la app ya tiene algún permiso de ubicación concedido (FINE o COARSE)
    public boolean hasLocationPermission() {
        boolean fine = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        return fine || coarse;
    }

    // Lanza el diálogo de permisos en tiempo de ejecución (foreground only)
    public void requestPermissions() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQ_PERMISSIONS);
    }

    // Comprueba la configuración de ubicación del dispositivo (GPS, redes, etc.)
    // Si falta algo resolvible, muestra el diálogo del sistema para activarlo.
    public void ensureSettingsOrResolve() {
        LocationSettingsRequest settingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(request)
                .build();

        SettingsClient client = LocationServices.getSettingsClient(activity);
        client.checkLocationSettings(settingsRequest)
                .addOnFailureListener(e -> {
                    // ResolvableApiException => el sistema puede mostrar un diálogo para arreglarlo (activar GPS, etc.)
                    if (e instanceof ResolvableApiException) {
                        try {
                            ((ResolvableApiException) e).startResolutionForResult(activity, REQ_RESOLVE_SETTINGS);
                        } catch (Exception ex) {
                            Log.e("LocationHelper", "No se pudo resolver ajustes: " + ex.getMessage());
                        }
                    }
                });
    }

    // Obtiene la última ubicación conocida (puede ser null si no hay caché aún)
    // Incluye chequeo explícito de permisos y manejo de SecurityException por seguridad.
    public void getLastLocation(LastLocationCallback cb) {
        // 1) Chequeo explícito de permisos (requerido por lint y buenas prácticas)
        boolean hasFine  = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean hasCoarse = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!hasFine && !hasCoarse) {
            cb.onError("Sin permisos de ubicación");
            return;
        }

        // 2) Manejo defensivo: el permiso podría revocarse entre el check y la llamada
        try {
            fused.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            // Devolvemos lat/lng al callback
                            cb.onLocation(location.getLatitude(), location.getLongitude());
                        } else {
                            // Sin última ubicación en caché: suele ocurrir la primera vez o con GPS apagado
                            cb.onError("No hay última ubicación (activa GPS o prueba al aire libre).");
                        }
                    })
                    .addOnFailureListener(e -> cb.onError("Error de ubicación: " + e.getMessage()));
        } catch (SecurityException se) {
            // Por si algo cambia en tiempo de ejecución y la llamada lanza SecurityException
            cb.onError("Permisos insuficientes: " + se.getMessage());
        }
    }

}

