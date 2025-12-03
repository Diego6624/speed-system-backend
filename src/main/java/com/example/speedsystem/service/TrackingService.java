package com.example.speedsystem.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.speedsystem.entities.PuntoRecorrido;
import com.example.speedsystem.entities.Recorrido;
import com.example.speedsystem.entities.Usuario;
import com.example.speedsystem.repository.RecorridoRepository;
import com.example.speedsystem.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final RecorridoRepository recorridoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PuntoRecorridoService puntoRecorridoService;

    public Recorrido iniciarRecorrido(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        Optional<Recorrido> activo = recorridoRepository.findByUsuarioIdAndActivoTrue(usuarioId);
        if (activo.isPresent()) {
            return activo.get(); // devolver el recorrido activo si ya existe
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

        if (!recorrido.getActivo()) return;

        PuntoRecorrido punto = puntoRecorridoService.registrarPunto(recorrido, lat, lng, velocidad);

        if (Boolean.TRUE.equals(punto.getExceso())) {
            recorrido.setExcesosVelocidad(recorrido.getExcesosVelocidad() + 1);
            recorridoRepository.save(recorrido);
        }
    }

    public Recorrido finalizarRecorrido(Long recorridoId) {
        Recorrido recorrido = recorridoRepository.findById(recorridoId)
                .orElseThrow(() -> new RuntimeException("Recorrido no encontrado"));

        recorrido.setActivo(false);
        recorrido.setFechaFin(LocalDateTime.now());

        List<PuntoRecorrido> puntos = puntoRecorridoService.listarPorRecorrido(recorridoId);
        if (!puntos.isEmpty()) {
            double distancia = calcularDistancia(puntos);
            double maxVel = puntos.stream().mapToDouble(PuntoRecorrido::getVelocidad).max().orElse(0);
            double promVel = puntos.stream().mapToDouble(PuntoRecorrido::getVelocidad).average().orElse(0);

            recorrido.setDistanciaKm(distancia);
            recorrido.setVelocidadMax(maxVel);
            recorrido.setVelocidadProm(promVel);
        }

        return recorridoRepository.save(recorrido);
    }

    // 🔧 Nuevo método: análisis semanal
    public AnalisisSemanal obtenerAnalisisSemanal(Long usuarioId) {
        LocalDateTime inicioSemana = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        List<Recorrido> recorridos = recorridoRepository.findByUsuarioIdAndFechaInicioAfter(usuarioId, inicioSemana);

        double totalKm = recorridos.stream()
                .mapToDouble(r -> r.getDistanciaKm() != null ? r.getDistanciaKm() : 0)
                .sum();

        long totalMin = recorridos.stream()
                .mapToLong(r -> r.getDuracionMin() != null ? r.getDuracionMin() : 0)
                .sum();

        double maxVel = recorridos.stream()
                .mapToDouble(r -> r.getVelocidadMax() != null ? r.getVelocidadMax() : 0)
                .max()
                .orElse(0);

        double promVel = recorridos.stream()
                .mapToDouble(r -> r.getVelocidadProm() != null ? r.getVelocidadProm() : 0)
                .average()
                .orElse(0);

        int excesos = recorridos.stream()
                .mapToInt(r -> r.getExcesosVelocidad() != null ? r.getExcesosVelocidad() : 0)
                .sum();

        return new AnalisisSemanal(totalKm, totalMin, maxVel, promVel, excesos);
    }

    // 🔧 Método auxiliar para calcular distancia total
    private double calcularDistancia(List<PuntoRecorrido> puntos) {
        double total = 0.0;
        for (int i = 1; i < puntos.size(); i++) {
            PuntoRecorrido p1 = puntos.get(i - 1);
            PuntoRecorrido p2 = puntos.get(i);
            total += haversine(p1.getLat(), p1.getLng(), p2.getLat(), p2.getLng());
        }
        return total;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // radio de la Tierra en km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // DTO interno para devolver el análisis semanal
    public record AnalisisSemanal(
            double totalKm,
            long totalMin,
            double velocidadMax,
            double velocidadProm,
            int excesosVelocidad
    ) {}

    public List<Recorrido> obtenerHistorial(Long usuarioId) {
    return recorridoRepository.findByUsuarioIdOrderByFechaInicioDesc(usuarioId);
}
}
