package com.dreamportal.ai;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;


public class AiClassifier {

    private static final String BASE_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String MODEL = "nvidia/nemotron-3.5-lightning:free";

    private static final HttpClient HTTP = HttpClient.newHttpClient();

    public Map<String, String> classify(String[] dreamNames) {
        String apiKey = Env.get("OPENROUTER_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENROUTER_API_KEY is not set. Add it to .env (see env.example).");
        }

        // the model collapses duplicate names, so send each dream only once
        LinkedHashSet<String> unique = new LinkedHashSet<>(Arrays.asList(dreamNames));
        ArrayList<String> uniqueList = new ArrayList<>(unique);
        String dreamNamesJson = new JSONArray(uniqueList).toString();

        JSONObject body = new JSONObject();
        body.put("model", MODEL)
            .put("temperature", 0)
            .put("messages", new JSONArray()
                .put(new JSONObject()
                    .put("role", "system")
                    .put("content", "Classify each dream as Good or Bad. "
                            + "Respond with ONLY a JSON array of strings, one per dream, "
                            + "in the same order as given. Each string must be exactly \"Good\" or \"Bad\". "
                            + "Example: [\"Good\",\"Bad\",\"Good\"]"))
                .put(new JSONObject()
                    .put("role", "user")
                    .put("content", dreamNamesJson)));

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(180))
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("API returned " + response.statusCode() + ": " + response.body());
            }

            String content = new JSONObject(response.body())
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            JSONArray arr;
            try {
                if (content.trim().startsWith("[")) {
                    arr = new JSONArray(content);
                } else {
                    arr = new JSONObject(content).getJSONArray("classification");
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not parse model output. Raw content was: " + content, e);
            }

            if (arr.length() != uniqueList.size()) {
                throw new IllegalStateException("Expected " + uniqueList.size()
                        + " classifications but got " + arr.length() + ": " + content);
            }

            Map<String, String> result = new LinkedHashMap<>();
            for (int i = 0; i < arr.length(); i++) {
                result.put(uniqueList.get(i), arr.getString(i));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("AI call failed:", e);
        }
    }
}
