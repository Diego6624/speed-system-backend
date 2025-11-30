package com.example.speedsystem.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.speedsystem.entities.PuntoRecorrido;
import com.example.speedsystem.entities.Recorrido;
import com.example.speedsystem.entities.Usuario;
import com.example.speedsystem.repository.PuntoRecorridoRepository;
import com.example.speedsystem.repository.RecorridoRepository;
import com.example.speedsystem.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final RecorridoRepository recorridoRepository;
    private final PuntoRecorridoRepository puntoRepo;
    private final UsuarioRepository usuarioRepository;

    public Recorrido iniciarRecorrido(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        System.out.println("🚀 Iniciando recorrido para usuario ID: " + usuarioId);

        Optional<Recorrido> activo = recorridoRepository.findByUsuarioIdAndActivoTrue(usuarioId);
        if (activo.isPresent()) {
            System.out.println("⚠️ Ya existe recorrido activo con ID: " + activo.get().getId());
            return activo.get();
        }

        Recorrido recorrido = new Recorrido();
        recorrido.setUsuario(usuario);
        recorrido.setFechaInicio(LocalDateTime.now());
        recorrido.setActivo(true);
        recorrido.setExcesosVelocidad(0);

        return recorridoRepository.save(recorrido);
    }

    public void registrarPunto(Long recorridoId, Double lat, Double lng, Double velocidad) {
        Recorrido recorrido = recorridoRepository.findById(recorridoId)
                .orElseThrow(() -> new RuntimeException("Recorrido no existe"));

        if (!recorrido.getActivo())
            return; // ignorar si ya fue cerrado

        PuntoRecorrido p = new PuntoRecorrido();
        p.setRecorrido(recorrido);
        p.setLat(lat);
        p.setLng(lng);
        p.setVelocidad(velocidad);
        p.setTimestamp(LocalDateTime.now());

        puntoRepo.save(p);
    }

    public Recorrido finalizarRecorrido(Long recorridoId) {
        Recorrido recorrido = recorridoRepository.findById(recorridoId)
                .orElseThrow(() -> new RuntimeException("Recorrido no encontrado"));

        recorrido.setActivo(false);
        recorrido.setFechaFin(LocalDateTime.now());

        return recorridoRepository.save(recorrido);
    }
}
