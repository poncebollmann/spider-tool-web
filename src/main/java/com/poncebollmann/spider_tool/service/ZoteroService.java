package com.poncebollmann.spider_tool.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.poncebollmann.spider_tool.model.ZoteroItem;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Conecta con la API de Zotero.
 * Obtiene colecciones e ítems de la biblioteca del usuario.
 */
@Service
public class ZoteroService {

    private static final String BASE_URL = "https://api.zotero.org";
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Obtiene las colecciones activas con ítems del usuario.
     */
    public List<ZoteroItem> getColecciones(String userId, String apiKey) throws IOException {
        String url = BASE_URL + "/users/" + userId + "/collections";
        String json = hacerPeticion(url, apiKey);

        List<ZoteroItem> colecciones = new ArrayList<>();
        JsonArray array = JsonParser.parseString(json).getAsJsonArray();

        for (JsonElement el : array) {
            JsonObject obj = el.getAsJsonObject();
            JsonObject data = obj.getAsJsonObject("data");

            // Saltar colecciones borradas
            if (data.has("deleted") && data.get("deleted").getAsBoolean()) continue;

            JsonObject meta = obj.getAsJsonObject("meta");
            int numItems = meta.get("numItems").getAsInt();
            if (numItems == 0) continue;

            ZoteroItem coleccion = new ZoteroItem();
            coleccion.setKey(data.get("key").getAsString());
            coleccion.setTitulo(data.get("name").getAsString());
            colecciones.add(coleccion);
        }

        return colecciones;
    }

    /**
     * Obtiene todos los ítems de una colección.
     * Pagina automáticamente si hay más de 100 ítems.
     */
    public List<ZoteroItem> getItems(String userId, String apiKey, String collectionKey) throws IOException {
        List<ZoteroItem> items = new ArrayList<>();
        int start = 0;
        int limit = 100;

        while (true) {
            String url = BASE_URL + "/users/" + userId + "/collections/"
                    + collectionKey + "/items?limit=" + limit + "&start=" + start
                    + "&itemType=-attachment";

            String json = hacerPeticion(url, apiKey);
            JsonArray array = JsonParser.parseString(json).getAsJsonArray();

            if (array.isEmpty()) break;

            for (JsonElement el : array) {
                JsonObject obj = el.getAsJsonObject();
                JsonObject data = obj.getAsJsonObject("data");

                ZoteroItem item = new ZoteroItem();
                item.setKey(data.get("key").getAsString());

                // Título
                item.setTitulo(data.has("title")
                        ? data.get("title").getAsString() : "Sin título");

                // Año
                if (data.has("date") && !data.get("date").getAsString().isEmpty()) {
                    String fecha = data.get("date").getAsString();
                    try {
                        item.setAnio(Integer.parseInt(fecha.substring(0, 4)));
                    } catch (Exception e) {
                        item.setAnio(0);
                    }
                }

                // Autores
                if (data.has("creators")) {
                    JsonArray creators = data.getAsJsonArray("creators");
                    StringBuilder autores = new StringBuilder();
                    for (JsonElement creator : creators) {
                        JsonObject c = creator.getAsJsonObject();
                        if (c.has("lastName")) {
                            if (autores.length() > 0) autores.append(", ");
                            autores.append(c.get("lastName").getAsString());
                            if (c.has("firstName")) {
                                autores.append(" ").append(c.get("firstName").getAsString());
                            }
                        }
                    }
                    item.setAutores(autores.toString());
                }

                // Revista
                if (data.has("publicationTitle")) {
                    item.setRevista(data.get("publicationTitle").getAsString());
                } else if (data.has("bookTitle")) {
                    item.setRevista(data.get("bookTitle").getAsString());
                }

                // DOI
                if (data.has("DOI")) {
                    item.setDoi(data.get("DOI").getAsString());
                }

                // Estado inicial
                item.setDecision("Pending");
                item.setEstado("Pending");

                items.add(item);
            }

            if (array.size() < limit) break;
            start += limit;
        }

        return items;
    }

    private String hacerPeticion(String url, String apiKey) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("Zotero-API-Key", apiKey)
                .header("Zotero-API-Version", "3")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error API Zotero: " + response.code());
            }
            return response.body().string();
        }
    }
}
