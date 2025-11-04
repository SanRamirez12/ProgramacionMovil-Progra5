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

    public static final int REQ_PERMISSIONS = 2001;
    public static final int REQ_RESOLVE_SETTINGS = 2002;

    private final Activity activity;
    private final FusedLocationProviderClient fused;
    private final LocationRequest request;

    public interface LastLocationCallback {
        void onLocation(double lat, double lng);
        void onError(String message);
    }

    public LocationHelper(Activity activity) {
        this.activity = activity;
        this.fused = LocationServices.getFusedLocationProviderClient(activity);
        // Balanced = mejor batería para este demo
        this.request = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 5000L)
                .setMinUpdateIntervalMillis(2000L)
                .setWaitForAccurateLocation(false)
                .build();
    }

    public boolean hasLocationPermission() {
        boolean fine = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        return fine || coarse;
    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQ_PERMISSIONS);
    }

    public void ensureSettingsOrResolve() {
        LocationSettingsRequest settingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(request)
                .build();
        SettingsClient client = LocationServices.getSettingsClient(activity);
        client.checkLocationSettings(settingsRequest)
                .addOnFailureListener(e -> {
                    if (e instanceof ResolvableApiException) {
                        try {
                            ((ResolvableApiException) e).startResolutionForResult(activity, REQ_RESOLVE_SETTINGS);
                        } catch (Exception ex) {
                            Log.e("LocationHelper", "No se pudo resolver ajustes: " + ex.getMessage());
                        }
                    }
                });
    }

    public void getLastLocation(LastLocationCallback cb) {
        // 1) Chequeo explícito de permisos
        boolean hasFine  = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean hasCoarse = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!hasFine && !hasCoarse) {
            cb.onError("Sin permisos de ubicación");
            return;
        }

        // 2) Manejo defensivo por si el permiso es revocado entre chequeo y llamada
        try {
            fused.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            cb.onLocation(location.getLatitude(), location.getLongitude());
                        } else {
                            cb.onError("No hay última ubicación (activa GPS o prueba al aire libre).");
                        }
                    })
                    .addOnFailureListener(e -> cb.onError("Error de ubicación: " + e.getMessage()));
        } catch (SecurityException se) {
            cb.onError("Permisos insuficientes: " + se.getMessage());
        }
    }

}
