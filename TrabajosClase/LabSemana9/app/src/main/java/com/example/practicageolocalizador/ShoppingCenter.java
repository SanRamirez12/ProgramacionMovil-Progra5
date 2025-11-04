package com.example.practicageolocalizador;

/**
 * Modelo inmutable que representa un centro comercial.
 * Contiene identificador, nombre, provincia/cantón, coordenadas (lat/lng) y un comentario opcional.
 */
public class ShoppingCenter {
    // Identificador único (por ejemplo, atributo "id" del XML)
    public final String id;
    // Nombre visible del centro comercial
    public final String nombre;
    // Provincia / ubicación descriptiva
    public final String provincia;
    // Latitud en grados decimales
    public final double lat;
    // Longitud en grados decimales
    public final double lng;
    // Comentario o nota adicional (puede venir vacío)
    public final String comentario;

    /**
     * Constructor que inicializa todas las propiedades.
     *
     * @param id          identificador único (no nulo)
     * @param nombre      nombre del centro (no nulo)
     * @param provincia   provincia o detalle de ubicación
     * @param lat         latitud en grados decimales
     * @param lng         longitud en grados decimales
     * @param comentario  texto opcional con observaciones
     */
    public ShoppingCenter(String id, String nombre, String provincia, double lat, double lng, String comentario) {
        this.id = id;
        this.nombre = nombre;
        this.provincia = provincia;
        this.lat = lat;
        this.lng = lng;
        this.comentario = comentario;
    }
}
