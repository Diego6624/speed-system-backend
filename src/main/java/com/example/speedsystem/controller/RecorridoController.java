package com.example.speedsystem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.speedsystem.dto.RecorridoResponse;
import com.example.speedsystem.dto.RecorridoSemanalResponse;
import com.example.speedsystem.entities.Usuario;
import com.example.speedsystem.service.RecorridoService;
import com.example.speedsystem.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class RecorridoController {

    private final RecorridoService recorridoService;
    private final UsuarioService usuarioService;

    @GetMapping("/my")
    public ResponseEntity<List<RecorridoResponse>> getRecorrido(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        Usuario usuario = usuarioService.getPorEmail(user.getUsername());
        return ResponseEntity.ok(recorridoService.getRecorrido(usuario.getId()));
    }

    @GetMapping("/my/weekly-stats")
    public ResponseEntity<RecorridoSemanalResponse> getRecorridoSemanalResponse(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        Usuario usuario = usuarioService.getPorEmail(user.getUsername());
        return ResponseEntity.ok(recorridoService.getRecorridoSemanalResponse(usuario.getId()));
    }
}