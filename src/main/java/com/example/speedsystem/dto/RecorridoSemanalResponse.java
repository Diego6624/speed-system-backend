package com.example.speedsystem.dto;

public record RecorridoSemanalResponse(
        double velocidadPromSemanal,
        double kilometrosRecorridos,
        int excesosVelocidad
) {}