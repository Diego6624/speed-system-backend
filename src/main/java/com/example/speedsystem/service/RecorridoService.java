package com.example.speedsystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.speedsystem.dto.RecorridoResponse;
import com.example.speedsystem.dto.RecorridoSemanalResponse;
import com.example.speedsystem.entities.Recorrido;
import com.example.speedsystem.repository.RecorridoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecorridoService {

    private final RecorridoRepository recorridoRepository;

    public List<RecorridoResponse> getRecorrido(Long usuarioId) {
        return recorridoRepository
                .findByUsuarioIdOrderByFechaInicioDesc(usuarioId)
                .stream()
                .map(r -> new RecorridoResponse(
                        r.getId(),
                        r.getFechaInicio(),
                        r.getFechaFin(),
                        r.getDistanciaKm(),
                        r.getVelocidadMax(),
                        r.getVelocidadProm(),
                        r.getExcesosVelocidad(),
                        r.getDuracionMin()))
                .toList();
    }

    public RecorridoSemanalResponse getRecorridoSemanalResponse(Long usuarioId) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        List<Recorrido> recorridos = recorridoRepository.findByUsuarioIdAndFechaInicioAfter(usuarioId, sevenDaysAgo);

        double distanciaTotal = recorridos.stream()
                .mapToDouble(Recorrido::getDistanciaKm)
                .sum();

        double velPromedio = recorridos.stream()
                .mapToDouble(Recorrido::getVelocidadProm)
                .average()
                .orElse(0);

        int excesosTotal = recorridos.stream()
                .mapToInt(Recorrido::getExcesosVelocidad)
                .sum();

        return new RecorridoSemanalResponse(
                velPromedio,
                distanciaTotal,
                excesosTotal);
    }

    public Recorrido obtenerPorId(Long recorridoId) {
        return recorridoRepository.findById(recorridoId)
                .orElseThrow(() -> new RuntimeException("Recorrido no encontrado con ID: " + recorridoId));
    }
}
