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

                List<Recorrido> recorridos = recorridoRepository.findByUsuarioIdAndFechaInicioAfter(usuarioId,
                                sevenDaysAgo);

                double distanciaTotal = recorridos.stream()
                                .filter(r -> r.getDistanciaKm() != null)
                                .mapToDouble(Recorrido::getDistanciaKm)
                                .sum();
                System.out.println("📊 distanciaTotal: " + distanciaTotal);
                double velPromedio = recorridos.stream()
                                .filter(r -> r.getVelocidadProm() != null)
                                .mapToDouble(Recorrido::getVelocidadProm)
                                .average()
                                .orElse(0);
                System.out.println("📊 velPromedio: " + velPromedio);
                int excesosTotal = recorridos.stream()
                                .filter(r -> r.getExcesosVelocidad() != null)
                                .mapToInt(Recorrido::getExcesosVelocidad)
                                .sum();
                System.out.println("📊 excesosTotal: " + excesosTotal);
                return new RecorridoSemanalResponse(
                                velPromedio,
                                distanciaTotal,
                                excesosTotal);

        }

        public Recorrido obtenerPorId(Long recorridoId) {
                return recorridoRepository.findById(recorridoId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Recorrido no encontrado con ID: " + recorridoId));
        }
}
