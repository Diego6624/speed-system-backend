package com.example.speedsystem.dto;

import java.time.LocalDateTime;

public record RecorridoResponse(
        Long id,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        Double distanciaKm,
        Double velocidadMax,
        Double velocidadProm,
        Integer excesosVelocidad,
        Long duracionMin
) {}