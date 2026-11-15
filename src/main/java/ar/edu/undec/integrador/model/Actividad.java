package ar.edu.undec.integrador.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "actividades")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "no debe estar vacío")
    private String nombre;

    @NotNull(message = "es obligatoria")
    @Future(message = "debe ser una fecha futura")
    private LocalDateTime fechaHora;

    @NotNull(message = "es obligatorio")
    @Positive(message = "debe ser mayor que cero")
    private Integer cupo;

    @Transient
    private Integer lugaresDisponibles;

    public Actividad() {}

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public Integer getCupo() { return cupo; }
    public Integer getLugaresDisponibles() {return lugaresDisponibles;}

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public void setCupo(Integer cupo) { this.cupo = cupo; }
    public void setLugaresDisponibles(Integer lugaresDisponibles) {this.lugaresDisponibles = lugaresDisponibles;}
}