package com.example.speedsystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.speedsystem.entities.PuntoRecorrido;
import com.example.speedsystem.entities.Recorrido;
import com.example.speedsystem.repository.PuntoRecorridoRepository;

@Service
public class PuntoRecorridoService {

    private final PuntoRecorridoRepository puntoRecorridoRepository;
    private final ZonaVelocidadService zonaVelocidadService;

    public PuntoRecorridoService(
            PuntoRecorridoRepository puntoRecorridoRepository,
            ZonaVelocidadService zonaVelocidadService) {

        this.puntoRecorridoRepository = puntoRecorridoRepository;
        this.zonaVelocidadService = zonaVelocidadService;
    }

    public PuntoRecorrido registrarPunto(Recorrido recorrido, double lat, double lng, double velocidadVehiculo) {
        // Obtener velocidad máxima de la zona
        Integer velMax = zonaVelocidadService.obtenerVelocidadMaxima(lat, lng);
        boolean hayExceso = (velMax != null && velocidadVehiculo > velMax);

        // Crear el punto
        PuntoRecorrido punto = new PuntoRecorrido();
        punto.setLat(lat);
        punto.setLng(lng);
        punto.setVelocidad(velocidadVehiculo);
        punto.setTimestamp(LocalDateTime.now());
        punto.setExceso(hayExceso);
        punto.setRecorrido(recorrido);

        return puntoRecorridoRepository.save(punto);
    }

    public List<PuntoRecorrido> listarPorRecorrido(Long recorridoId) {
        return puntoRecorridoRepository.findByRecorridoId(recorridoId);
    }
}
