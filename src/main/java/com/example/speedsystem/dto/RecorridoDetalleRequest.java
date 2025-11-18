package com.example.speedsystem.dto;

import lombok.Data;

@Data
public class RecorridoDetalleRequest {
    private Double latitud;
    private Double longitud;
    private Double velocidad;
    private Boolean exceso;
}
