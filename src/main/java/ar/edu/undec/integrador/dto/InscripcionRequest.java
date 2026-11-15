package ar.edu.undec.integrador.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class InscripcionRequest {

    @NotNull(message = "es obligatorio")
    @Positive(message = "debe ser un ID válido")
    private Long actividadId;

    @NotNull(message = "es obligatorio")
    @Positive(message = "debe ser un ID válido")
    private Long socioId;

    public Long getActividadId() { return actividadId; }
    public Long getSocioId() { return socioId; }

    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }
    public void setSocioId(Long socioId) { this.socioId = socioId; }
}