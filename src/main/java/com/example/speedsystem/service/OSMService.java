package com.example.speedsystem.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

@Service
public class OSMService {

    private static final String OVERPASS_API_URL = "https://overpass-api.de/api/interpreter";

    public Integer obtenerVelocidadMaxima(double lat, double lon) {
        try {
            String query = """
                [out:json];
                way(around:25,%f,%f)["highway"];
                out tags;
                """.formatted(lat, lon);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OVERPASS_API_URL + "?data=" + query.replace("\n", "")))
                    .header("User-Agent", "MiAppEstudiante/1.0") // obligatorio para OSM
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return extraerMaxSpeed(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Integer extraerMaxSpeed(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        var elements = root.getAsJsonArray("elements");
        if (elements == null || elements.isEmpty()) return null;

        for (var element : elements) {
            JsonObject obj = element.getAsJsonObject();
            if (!obj.has("tags")) continue;
            Integer speed = extraerMaxSpeed(obj.get("tags").getAsJsonObject());
            if (speed != null) return speed;
        }

        return null;
    }

    private Integer extraerMaxSpeed(JsonObject tags) {
        if (tags == null) return null;

        // 1. maxspeed explícito
        if (tags.has("maxspeed")) {
            return parseSpeed(tags.get("maxspeed").getAsString());
        }

        // 2. maxspeed condicional
        if (tags.has("maxspeed:conditional")) {
            String val = tags.get("maxspeed:conditional").getAsString();
            String extracted = val.split(" ")[0];
            return parseSpeed(extracted);
        }

        return null;
    }

    private Integer parseSpeed(String raw) {
        raw = raw.replaceAll("[^0-9]", "");
        if (raw.isEmpty()) return null;
        return Integer.parseInt(raw);
    }
}
