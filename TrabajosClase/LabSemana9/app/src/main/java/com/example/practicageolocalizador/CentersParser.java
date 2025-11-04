package com.example.practicageolocalizador;

import android.content.Context;
import android.content.res.Resources;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CentersParser {

    /**
     * Parsea el XML de centros comerciales desde res/raw/centros.xml
     * y retorna una lista de ShoppingCenter.
     *
     * @param ctx Contexto para acceder a recursos.
     * @return Lista de ShoppingCenter extraídos del XML.
     * @throws Exception Propaga cualquier error de IO o parsing.
     */
    public static List<ShoppingCenter> parse(Context ctx) throws Exception {
        // Obtenemos los recursos para abrir el archivo en res/raw
        Resources res = ctx.getResources();
        // Abre el InputStream del recurso raw (el archivo debe llamarse "centros.xml")
        InputStream is = res.openRawResource(R.raw.centros);

        // Preparamos un XmlPullParser (parser por eventos: START_TAG, TEXT, END_TAG, etc.)
        XmlPullParser xpp = Xml.newPullParser();
        xpp.setInput(is, "UTF-8");

        // Lista resultado
        List<ShoppingCenter> list = new ArrayList<>();

        // Variables temporales para armar cada objeto ShoppingCenter
        String id = null, nombre = null, provincia = null, comentario = null;
        Double lat = null, lng = null;

        // Estado del parser y auxiliares
        int eventType = xpp.getEventType();
        String currentTag = null;  // nombre de la etiqueta actual (utilidad opcional)
        String text = null;        // último texto leído entre tags

        // Recorremos el documento hasta encontrar END_DOCUMENT
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    // Al iniciar una etiqueta, guardamos su nombre
                    currentTag = xpp.getName();

                    // Si comienza un <centro ...>, inicializamos variables del registro
                    if ("centro".equals(currentTag)) {
                        // Leemos el atributo id del tag <centro id="...">
                        id = xpp.getAttributeValue(null, "id");
                        // Limpiamos campos anteriores por seguridad
                        nombre = provincia = comentario = null;
                        lat = lng = null;
                    }
                    break;

                case XmlPullParser.TEXT:
                    // El parser entrega el texto contenido entre un START_TAG y su END_TAG
                    // Lo almacenamos para usarlo cuando llegue el END_TAG correspondiente
                    text = xpp.getText();
                    break;

                case XmlPullParser.END_TAG:
                    // Al cerrar una etiqueta, decidimos qué campo completar
                    String endTag = xpp.getName();

                    // Mapeo de etiquetas hijas a campos del modelo temporal
                    if ("nombre".equals(endTag))       nombre = safe(text);
                    else if ("provincia".equals(endTag)) provincia = safe(text);
                    else if ("latitud".equals(endTag))   lat = parseD(text);
                    else if ("longitud".equals(endTag))  lng = parseD(text);
                    else if ("comentario".equals(endTag)) comentario = safe(text);

                        // Si cierra </centro>, validamos y agregamos a la lista
                    else if ("centro".equals(endTag)) {
                        // Solo añadimos si los campos obligatorios están presentes
                        if (id != null && nombre != null && provincia != null && lat != null && lng != null) {
                            list.add(new ShoppingCenter(
                                    id, nombre, provincia, lat, lng,
                                    comentario == null ? "" : comentario));
                        }
                        // Reiniciamos variables para el próximo <centro>
                        id = nombre = provincia = comentario = null;
                        lat = lng = null;
                    }

                    // Limpiamos auxiliares de texto/etiqueta actual
                    text = null; currentTag = null;
                    break;
            }

            // Avanzamos al siguiente evento del XML
            eventType = xpp.next();
        }

        // Cerramos el stream y devolvemos la lista
        is.close();
        return list;
    }

    /**
     * Devuelve el string sin espacios o vacío si es null.
     */
    private static String safe(String s) { return s == null ? "" : s.trim(); }

    /**
     * Parsea un Double desde string, devolviendo null si falla.
     */
    private static Double parseD(String s) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return null; }
    }
}
