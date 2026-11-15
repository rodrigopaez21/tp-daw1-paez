
package ar.edu.undec.integrador.service;

import ar.edu.undec.integrador.dto.InscripcionRequest;
import ar.edu.undec.integrador.exception.*;
import ar.edu.undec.integrador.model.*;
import ar.edu.undec.integrador.model.Inscripcion.Estado;
import ar.edu.undec.integrador.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InscripcionService {

    private final InscripcionRepository repository;
    private final ActividadRepository actividadRepository;
    private final SocioRepository socioRepository;

    public InscripcionService(
            InscripcionRepository repository,
            ActividadRepository actividadRepository,
            SocioRepository socioRepository) {
        this.repository = repository;
        this.actividadRepository = actividadRepository;
        this.socioRepository = socioRepository;
    }

    @Transactional
    public Inscripcion crear(InscripcionRequest request) {

        Actividad actividad = actividadRepository.findById(
                request.getActividadId()).orElseThrow(() ->
                new RecursoNoEncontradoException("Actividad inexistente"));

        Socio socio = socioRepository.findById(
                request.getSocioId()).orElseThrow(() ->
                new RecursoNoEncontradoException("Socio inexistente"));

        if (repository.existsByActividadIdAndSocioIdAndEstadoNot(
                actividad.getId(), socio.getId(), Estado.CANCELADA)) {
            throw new ReglaNegocioException(
                    "El socio ya está inscripto en esta actividad");
        }

        long ocupados = repository.countByActividadIdAndEstadoNot(
                actividad.getId(), Estado.CANCELADA);

        if (ocupados >= actividad.getCupo()) {
            throw new ReglaNegocioException(
                    "La actividad no tiene cupos disponibles");
        }

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setActividad(actividad);
        inscripcion.setSocio(socio);
        inscripcion.setEstado(Estado.CONFIRMADA);

        return repository.save(inscripcion);
    }

    public List<Inscripcion> listarPorActividad(Long actividadId) {

        if (!actividadRepository.existsById(actividadId)) {
            throw new RecursoNoEncontradoException(
                    "Actividad inexistente");
        }

        return repository.findByActividadId(actividadId);
    }

    @Transactional
    public Inscripcion cambiarEstado(Long id, String nuevoEstado) {

        Inscripcion inscripcion = repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Inscripción inexistente"));

        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new IllegalArgumentException(
                    "El estado es obligatorio");
        }

        Estado destino;

        try {
            destino = Estado.valueOf(nuevoEstado);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "El estado debe ser CANCELADA o ASISTIO");
        }

        if (destino != Estado.CANCELADA && destino != Estado.ASISTIO) {
            throw new IllegalArgumentException(
                    "El estado debe ser CANCELADA o ASISTIO");
        }

        // Solo se permiten cambios desde CONFIRMADA.
        if (inscripcion.getEstado() != Estado.CONFIRMADA) {
            throw new ReglaNegocioException(
                    "La inscripción ya se encuentra en un estado final");
        }

        // No se puede cancelar con menos de 2 horas de anticipación.
        if (destino == Estado.CANCELADA) {

            LocalDateTime fechaActividad =
                    inscripcion.getActividad().getFechaHora();

            Duration tiempoRestante = Duration.between(
                    LocalDateTime.now(), fechaActividad);

            if (tiempoRestante.compareTo(Duration.ofHours(2)) < 0) {
                throw new ReglaNegocioException(
                        "No se puede cancelar con menos de 2 horas de anticipación");
            }
        }

        inscripcion.setEstado(destino);

        return repository.save(inscripcion);
    }
}
