package com.example.practicageolocalizador;

import android.content.Context;
import android.content.res.Resources;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CentersParser {

    public static List<ShoppingCenter> parse(Context ctx) throws Exception {
        Resources res = ctx.getResources();
        InputStream is = res.openRawResource(R.raw.centros);
        XmlPullParser xpp = Xml.newPullParser();
        xpp.setInput(is, "UTF-8");

        List<ShoppingCenter> list = new ArrayList<>();
        String id = null, nombre = null, provincia = null, comentario = null;
        Double lat = null, lng = null;

        int eventType = xpp.getEventType();
        String currentTag = null;
        String text = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    currentTag = xpp.getName();
                    if ("centro".equals(currentTag)) {
                        id = xpp.getAttributeValue(null, "id");
                        nombre = provincia = comentario = null;
                        lat = lng = null;
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = xpp.getText();
                    break;

                case XmlPullParser.END_TAG:
                    String endTag = xpp.getName();
                    if ("nombre".equals(endTag))       nombre = safe(text);
                    else if ("provincia".equals(endTag)) provincia = safe(text);
                    else if ("latitud".equals(endTag))   lat = parseD(text);
                    else if ("longitud".equals(endTag))  lng = parseD(text);
                    else if ("comentario".equals(endTag)) comentario = safe(text);
                    else if ("centro".equals(endTag)) {
                        if (id != null && nombre != null && provincia != null && lat != null && lng != null) {
                            list.add(new ShoppingCenter(id, nombre, provincia, lat, lng, comentario == null ? "" : comentario));
                        }
                        id = nombre = provincia = comentario = null;
                        lat = lng = null;
                    }
                    text = null; currentTag = null;
                    break;
            }
            eventType = xpp.next();
        }
        is.close();
        return list;
    }

    private static String safe(String s) { return s == null ? "" : s.trim(); }
    private static Double parseD(String s) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return null; }
    }
}
