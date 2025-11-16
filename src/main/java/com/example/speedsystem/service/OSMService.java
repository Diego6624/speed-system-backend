package com.example.speedsystem.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

@Service
public class OSMService {

    private final Gson gson = new Gson();

    public Integer obtenerVelocidadMaxima(double lat, double lon) {

        String query = """
                    [out:json][timeout:10];
                    way["highway"](around:40,%f,%f);
                    out tags qt;
                """.formatted(lat, lon);

        try {
            URL url = new URL("https://overpass-api.de/api/interpreter");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String body = "data=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
            conn.getOutputStream().write(body.getBytes());

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            JsonObject json = gson.fromJson(br, JsonObject.class);
            JsonArray elements = json.getAsJsonArray("elements");

            if (elements == null || elements.size() == 0)
                return null;

            JsonObject way = elements.get(0).getAsJsonObject();
            JsonObject tags = way.getAsJsonObject("tags");

            return extraerMaxSpeed(tags);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Integer extraerMaxSpeed(JsonObject tags) {
        if (tags == null)
            return null;

        // 1. Si existe maxspeed, usarlo
        if (tags.has("maxspeed")) {
            return parseSpeed(tags.get("maxspeed").getAsString());
        }

        // 2. Si existe maxspeed:conditional, tomar el primer valor numérico
        if (tags.has("maxspeed:conditional")) {
            String val = tags.get("maxspeed:conditional").getAsString();
            return parseSpeed(val.split(" ")[0]);
        }

        // 3. Si existe fuente de velocidad por defecto (source:maxspeed)
        if (tags.has("source:maxspeed")) {
            return velocidadPorDefecto(tags.get("source:maxspeed").getAsString());
        }

        // 4. Si conocemos el tipo de vía, aplicar velocidad por defecto
        if (tags.has("highway")) {
            String highway = tags.get("highway").getAsString();
            return velocidadPorDefectoPorTipo(highway);
        }

        return null;
    }

    private Integer parseSpeed(String raw) {
        raw = raw.replaceAll("[^0-9]", "");
        if (raw.isEmpty())
            return null;
        return Integer.parseInt(raw);
    }

    private Integer velocidadPorDefecto(String src) {
        return switch (src) {
            case "PE:urban" -> 50;
            case "PE:rural" -> 90;
            default -> null;
        };
    }

    private Integer velocidadPorDefectoPorTipo(String highway) {
    return switch (highway) {
        case "trunk" -> 90;
        case "primary" -> 60;
        case "secondary" -> 50;
        case "tertiary" -> 40;
        case "residential" -> 30;
        default -> null;
    };
}
}
