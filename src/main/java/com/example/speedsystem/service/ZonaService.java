package com.example.speedsystem.service;

import com.example.speedsystem.entities.Zona;
import com.example.speedsystem.repository.ZonaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ZonaService {

    @Autowired
    private ZonaRepository zonaRepository;

    private final String OSM_URL = "https://nominatim.openstreetmap.org/search";

    public List<Zona> listarZonas() {
        return zonaRepository.findAll();
    }

    public Zona guardarZona(Zona zona) {
    // Construimos la URL para buscar la zona en OpenStreetMap (Nominatim)
    String url = String.format(
            "%s?q=%s&format=json&polygon_geojson=1",
            OSM_URL,
            zona.getNombre().replace(" ", "+")
    );

    RestTemplate restTemplate = new RestTemplate();

    // Llamamos a la API y especificamos el tipo de respuesta esperado (lista de mapas)
    ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
    );

    List<Map<String, Object>> response = responseEntity.getBody();

    // Procesamos la respuesta de Nominatim
    if (response != null && !response.isEmpty()) {
        Map<String, Object> data = response.get(0); // Tomamos el primer resultado

        // Obtenemos latitud y longitud
        if (data.get("lat") != null && data.get("lon") != null) {
            zona.setLatitud(Double.parseDouble(data.get("lat").toString()));
            zona.setLongitud(Double.parseDouble(data.get("lon").toString()));
        }

        // Guardamos el geojson (área delimitada)
        Object geojson = data.get("geojson");
        if (geojson != null) {
            zona.setLimite(geojson.toString());
        }
    }

    // Guardamos la zona en base de datos
    return zonaRepository.save(zona);
}


    public Zona obtenerZona(Long id) {
        return zonaRepository.findById(id).orElse(null);
    }
}