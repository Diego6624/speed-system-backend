package com.example.speedsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.speedsystem.entities.Zona;
import com.example.speedsystem.service.ZonaService;

@RestController
@RequestMapping("/api/zonas")
@CrossOrigin(origins = "*")
public class ZonaController {
    
    @Autowired
    private ZonaService zonaService;

    @GetMapping
    public List<Zona> listar() {
        return zonaService.listarZonas();
    }

    @PostMapping
    public Zona crear(@RequestBody Zona zona) {
        return zonaService.guardarZona(zona);
    }

    @GetMapping("/{id}")
    public Zona obtener(@PathVariable Long id) {
        return zonaService.obtenerZona(id);
    }
}
