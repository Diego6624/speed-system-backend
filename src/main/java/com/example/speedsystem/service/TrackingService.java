package com.example.speedsystem.service;

import java.time.LocalDateTime;
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
    private final PuntoRecorridoService puntoRecorridoService; // 🔑 delegamos aquí

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

        // 🔑 delegamos la creación del punto al PuntoRecorridoService
        PuntoRecorrido punto = puntoRecorridoService.registrarPunto(recorridoId, lat, lng, velocidad);

        // Si hubo exceso de velocidad, actualizamos el contador en el recorrido
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

        // Calcular métricas al finalizar
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
}
