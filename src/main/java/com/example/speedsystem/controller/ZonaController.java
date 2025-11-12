package com.example.speedsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.speedsystem.entities.Zona;
import com.example.speedsystem.service.ZonaService;

@RestController
@RequestMapping("api/zonas")
public class ZonaController {
    
    @Autowired
    private ZonaService zonaService;

    @GetMapping
    public List<Zona> listarZonas(){
        return zonaService.listarZona();
    }

    @GetMapping("/{id}")
    public Zona obtenerZona(@PathVariable Long id){
        return zonaService.obtenerZonaPorId(id)
            .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
    }

    
}
