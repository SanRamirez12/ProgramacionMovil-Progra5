package com.example.practicageolocalizador;

public class ShoppingCenter {
    public final String id;
    public final String nombre;
    public final String provincia;
    public final double lat;
    public final double lng;
    public final String comentario;

    public ShoppingCenter(String id, String nombre, String provincia, double lat, double lng, String comentario) {
        this.id = id;
        this.nombre = nombre;
        this.provincia = provincia;
        this.lat = lat;
        this.lng = lng;
        this.comentario = comentario;
    }
}
