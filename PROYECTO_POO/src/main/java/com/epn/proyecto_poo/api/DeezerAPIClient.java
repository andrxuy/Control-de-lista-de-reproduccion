package com.epn.proyecto_poo.api;

import com.epn.proyecto_poo.modelo.CancionPreview;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DeezerAPIClient {
    private static final String SEARCH_URL = "https://api.deezer.com/search?q=";
    private final OkHttpClient client;

    public DeezerAPIClient() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    public List<CancionPreview> buscarCanciones(String query) {
        List<CancionPreview> resultados = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) return resultados;

        try {
            String url = SEARCH_URL + java.net.URLEncoder.encode(query.trim(), "UTF-8") + "&limit=10";
            System.out.println("[Deezer] Buscando: " + query.trim());

            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.err.println("[Deezer] Error HTTP " + response.code());
                    return resultados;
                }

                String body = response.body() != null ? response.body().string() : "";
                JsonObject root = JsonParser.parseString(body).getAsJsonObject();

                if (!root.has("data")) return resultados;

                JsonArray data = root.getAsJsonArray("data");
                if (data == null || data.size() == 0) {
                    System.out.println("[Deezer] 0 resultados");
                    return resultados;
                }

                int conPreview = 0;
                for (int i = 0; i < data.size(); i++) {
                    JsonObject track = data.get(i).getAsJsonObject();
                    CancionPreview c = parsearTrack(track);
                    if (c != null) {
                        resultados.add(c);
                        if (c.getPreviewUrl() != null) conPreview++;
                    }
                }

                System.out.println("[Deezer] " + resultados.size() + " canciones, " +
                        conPreview + " con preview");
            }
        } catch (Exception e) {
            System.err.println("[Deezer] Error: " + e.getMessage());
        }

        return resultados;
    }

    private CancionPreview parsearTrack(JsonObject track) {
        try {
            String id = String.valueOf(track.get("id").getAsLong());
            String titulo = track.has("title") ? track.get("title").getAsString() : "";

            String artista = "";
            if (track.has("artist")) {
                artista = track.getAsJsonObject("artist").get("name").getAsString();
            }

            String album = "";
            String portadaUrl = null;
            if (track.has("album")) {
                JsonObject albumObj = track.getAsJsonObject("album");
                if (albumObj.has("title")) album = albumObj.get("title").getAsString();
                if (albumObj.has("cover_xl")) portadaUrl = albumObj.get("cover_xl").getAsString();
                else if (albumObj.has("cover_big")) portadaUrl = albumObj.get("cover_big").getAsString();
                else if (albumObj.has("cover_medium")) portadaUrl = albumObj.get("cover_medium").getAsString();
            }

            String previewUrl = null;
            if (track.has("preview") && !track.get("preview").isJsonNull()) {
                previewUrl = track.get("preview").getAsString();
            }

            int duracionMs = track.has("duration") ? track.get("duration").getAsInt() * 1000 : 0;

            return new CancionPreview(id, titulo, artista, album, previewUrl, portadaUrl, duracionMs);
        } catch (Exception e) {
            return null;
        }
    }

    public String buscarPreview(String titulo, String artista) {
        List<CancionPreview> r = buscarCanciones(titulo + " " + artista);
        return (!r.isEmpty()) ? r.get(0).getPreviewUrl() : null;
    }
}
