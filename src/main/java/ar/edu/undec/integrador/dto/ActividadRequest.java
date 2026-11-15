package ar.edu.undec.integrador.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class ActividadRequest {

    @NotBlank(message = "no debe estar vacío")
    private String nombre;

    @NotNull(message = "es obligatoria")
    @Future(message = "debe ser una fecha futura")
    private LocalDateTime fechaHora;

    @NotNull(message = "es obligatorio")
    @Positive(message = "debe ser mayor que cero")
    private Integer cupo;

    public String getNombre() { return nombre; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public Integer getCupo() { return cupo; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public void setCupo(Integer cupo) { this.cupo = cupo; }
}