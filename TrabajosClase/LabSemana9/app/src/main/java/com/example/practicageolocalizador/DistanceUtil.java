package com.example.practicageolocalizador;

public class DistanceUtil {
    // Calcula la distancia entre dos puntos geográficos usando la fórmula de Haversine.
    // Retorna la distancia en kilómetros.
    public static double haversineKm(double lat1, double lng1, double lat2, double lng2) {
        // Radio medio de la Tierra en km (WGS-84 aproximado)
        final double R = 6371.0088;

        // Diferencias angulares en radianes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        // Fórmula de Haversine:
        // a = sin²(Δφ/2) + cos φ1 * cos φ2 * sin²(Δλ/2)
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);

        // c = 2 * atan2(√a, √(1−a))
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distancia = R * c (en kilómetros)
        return R * c;
    }
}
