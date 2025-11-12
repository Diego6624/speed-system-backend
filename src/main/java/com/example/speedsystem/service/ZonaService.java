package com.example.speedsystem.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.speedsystem.entities.Zona;
import com.example.speedsystem.repository.ZonaRepository;

@Service
public class ZonaService {
    
    @Autowired
    private ZonaRepository zonaRepository;

    public List<Zona> listarZona(){
        return zonaRepository.findAll();
    }

    public Optional<Zona> obtenerZonaPorId(Long id){
        return zonaRepository.findById(id);
    }

    public Zona crearZona(Zona zona){
        return zonaRepository.save(zona);
    }

    public Zona actualizarZona(Long id, Zona zonaActualizada){
        return zonaRepository.findById(id)
            .map(zona -> {
                zona.setName(zonaActualizada.getName());
                zona.setLatitud(zonaActualizada.getLatitud());
                zona.setLongitud(zonaActualizada.getLongitud());
                zona.setSpeedLimit(zonaActualizada.getSpeedLimit());
                return zonaRepository.save(zona);
            })
            . orElseThrow(() -> new RuntimeException("Zona no encontrada"));
    }

    public void eliminarZona(Long id){
        zonaRepository.deleteById(id);
    }

    public Zona obtenerZonaCercana(double latitud, double longitud){
        return zonaRepository.findAll().stream()
            .min(Comparator.comparingDouble(
                z -> distancia(latitud, longitud, z.getLatitud(), z.getLongitud())
            ))
            .orElse(null);
    }

    private double distancia(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2));
    }

}
