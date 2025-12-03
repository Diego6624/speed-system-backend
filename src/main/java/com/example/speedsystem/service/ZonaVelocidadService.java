package com.example.speedsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ZonaVelocidadService {

    private final OSMService osmService;

    // 🗂️ Caché en memoria: clave = "lat,lon", valor = límite
    private final Map<String, Integer> cache = new ConcurrentHashMap<>();

    public Integer obtenerVelocidadMaxima(double lat, double lon) {
        String key = lat + "," + lon;

        // 1️⃣ Si ya está en caché → devolverlo
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        // 2️⃣ Si no está en caché → llamar a OSMService
        try {
            Integer limite = osmService.obtenerVelocidadMaxima(lat, lon);

            if (limite != null) {
                cache.put(key, limite); // guardar en caché
                return limite;
            } else {
                // 3️⃣ Fallback si OSM devuelve null
                return 40;
            }
        } catch (Exception e) {
            // 4️⃣ Fallback si Overpass falla (504, timeout, etc.)
            System.err.println("⚠️ Error consultando Overpass: " + e.getMessage());
            return 40;
        }
    }
}
