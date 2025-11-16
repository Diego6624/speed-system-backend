package com.example.speedsystem.service;

import org.springframework.stereotype.Service;

@Service
public class ZonaVelocidadService {

    private final OSMService osmService;

    public ZonaVelocidadService(OSMService osmService) {
        this.osmService = osmService;
    }

    public Integer obtenerVelocidadMaxima(double lat, double lon) {
        return osmService.obtenerVelocidadMaxima(lat, lon);
    }
}

