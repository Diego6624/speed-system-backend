package com.example.speedsystem.entities;

import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "recorrido")
public class Recorrido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Double distanciaKm;
    private Double velocidadMax;
    private Double velocidadProm;
    private Integer excesosVelocidad;
    private Boolean activo;
    
    public Long getDuracionMin() {
        if (fechaInicio == null || fechaFin == null) return 0L;
        return Duration.between(fechaInicio, fechaFin).toMinutes();
    }
}
