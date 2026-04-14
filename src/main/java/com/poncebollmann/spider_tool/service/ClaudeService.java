package com.poncebollmann.spider_tool.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.gson.*;

import java.io.IOException;

/**
 * Servicio que conecta con la API de Claude (Anthropic).
 * Envía mensajes y devuelve la respuesta del asistente.
 */
@Service
public class ClaudeService {

    @Value("${anthropic.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.anthropic.com/v1/messages";
    private static final String MODEL   = "claude-opus-4-5";
    private final OkHttpClient client   = new OkHttpClient();

    private static final String SYSTEM_PROMPT = """
        Eres un asistente especializado en revisiones sistemáticas de investigación.
        Tu rol es ayudar a investigadores a:
        - Construir y mejorar protocolos de búsqueda (SPIDER, PICO, PICOS)
        - Evaluar si un estudio cumple criterios de inclusión o exclusión
        - Sugerir sinónimos y términos MeSH para cadenas de búsqueda booleana
        - Interpretar y aplicar las guías PRISMA 2020
        - Evaluar la calidad metodológica de estudios
        - Resolver dudas sobre el proceso de revisión sistemática

        Responde siempre en el idioma del investigador.
        Sé preciso, conciso y cita estándares metodológicos cuando sea relevante.
        Si el investigador comparte un abstract o título, ayúdale a decidir
        si incluirlo o excluirlo según criterios PRISMA.
        """;

    /**
     * Envía un mensaje a Claude y devuelve la respuesta.
     *
     * @param mensaje      pregunta del investigador
     * @param contexto     contexto opcional (protocolo SPIDER, abstract, etc.)
     * @return             respuesta de Claude
     */
    public String chat(String mensaje, String contexto) throws IOException {

        String mensajeCompleto = contexto != null && !contexto.isBlank()
                ? "Contexto del investigador:\n" + contexto + "\n\nPregunta: " + mensaje
                : mensaje;

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", MODEL);
        requestBody.addProperty("max_tokens", 1024);

        JsonArray messages = new JsonArray();
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", mensajeCompleto);
        messages.add(userMessage);

        requestBody.add("messages", messages);
        requestBody.addProperty("system", SYSTEM_PROMPT);

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01")
                .header("content-type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error API Claude: " + response.code()
                        + " — " + response.body().string());
            }

            String responseJson = response.body().string();
            JsonObject parsed   = JsonParser.parseString(responseJson).getAsJsonObject();

            return parsed.getAsJsonArray("content")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();
        }
    }
}
