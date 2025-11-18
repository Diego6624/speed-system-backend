package com.example.speedsystem.entities;

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
@Table(name = "recorrido_detalle")
public class PuntoRecorrido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private LocalDateTime timestamp;
    private Double lat;
    private Double lng;
    private Double velocidad;
    private Boolean exceso;

    @ManyToOne
    @JoinColumn(name = "recorrido_id")
    private Recorrido recorrido;
}
