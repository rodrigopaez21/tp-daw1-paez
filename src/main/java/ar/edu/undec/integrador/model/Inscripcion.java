package ar.edu.undec.integrador.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inscripciones")
public class Inscripcion {

    public enum Estado {
        CONFIRMADA,
        ASISTIO,
        CANCELADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "actividad_id")
    private Actividad actividad;

    @ManyToOne(optional = false)
    @JoinColumn(name = "socio_id")
    private Socio socio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.CONFIRMADA;

    public Inscripcion() {}

    public Long getId() { return id; }
    public Actividad getActividad() { return actividad; }
    public Socio getSocio() { return socio; }
    public Estado getEstado() { return estado; }

    public void setId(Long id) { this.id = id; }
    public void setActividad(Actividad actividad) { this.actividad = actividad; }
    public void setSocio(Socio socio) { this.socio = socio; }
    public void setEstado(Estado estado) { this.estado = estado; }
}