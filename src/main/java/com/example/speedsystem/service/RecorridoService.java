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
        return recorridoRepository.findByUsuarioIdOrderByFechaInicioDesc(usuarioId)
                .stream()
                .map(t -> new RecorridoResponse(
                        t.getId(),
                        t.getFechaInicio(),
                        t.getFechaFin(),
                        t.getDistanciaKm(),
                        t.getVelocidadMax(),
                        t.getVelocidadProm(),
                        t.getExcesosVelocidad(),
                        t.getDuracionMin()
                )).toList();
    }

    public RecorridoSemanalResponse getRecorridoSemanalResponse(Long usuarioId) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        List<Recorrido> recorridos = recorridoRepository.findByUsuarioIdAndFechaInicioAfter(usuarioId, sevenDaysAgo);

        double km = recorridos.stream().mapToDouble(Recorrido::getDistanciaKm).sum();
        double velProm = recorridos.stream()
                .mapToDouble(Recorrido::getVelocidadProm)
                .average().orElse(0);

        int excesos = recorridos.stream()
                .mapToInt(Recorrido::getExcesosVelocidad)
                .sum();

        return new RecorridoSemanalResponse(velProm, km, excesos);
    }
}