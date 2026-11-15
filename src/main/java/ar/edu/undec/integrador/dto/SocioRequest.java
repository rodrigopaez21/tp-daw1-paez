package ar.edu.undec.integrador.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SocioRequest {

    @NotBlank(message = "no debe estar vacío")
    private String nombre;

    @NotBlank(message = "no debe estar vacío")
    @Email(message = "debe ser un email válido")
    private String email;

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
}