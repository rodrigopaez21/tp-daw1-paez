package ar.edu.undec.integrador.dto;

import jakarta.validation.constraints.NotBlank;

public class EstadoRequest {

    @NotBlank(message = "no debe estar vacío")
    private String estado;

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}