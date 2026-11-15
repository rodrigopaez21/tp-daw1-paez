
package ar.edu.undec.integrador.service;

import ar.edu.undec.integrador.model.Actividad;
import ar.edu.undec.integrador.model.Inscripcion.Estado;
import ar.edu.undec.integrador.repository.ActividadRepository;
import ar.edu.undec.integrador.repository.InscripcionRepository;
import ar.edu.undec.integrador.exception.RecursoNoEncontradoException;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final InscripcionRepository inscripcionRepository;

    public ActividadService(
            ActividadRepository actividadRepository,
            InscripcionRepository inscripcionRepository) {
        this.actividadRepository = actividadRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    public List<Actividad> listar() {
        List<Actividad> actividades = actividadRepository.findAll();

        actividades.forEach(this::calcularLugaresDisponibles);

        return actividades;
    }

    public Actividad buscarPorId(Long id) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Actividad no encontrada con ID: " + id));

        calcularLugaresDisponibles(actividad);

        return actividad;
    }

    public Actividad crear(Actividad actividad) {
        Actividad creada = actividadRepository.save(actividad);
        calcularLugaresDisponibles(creada);
        return creada;
    }

    public Actividad modificar(Long id, Actividad datos) {
        Actividad actividad = buscarPorId(id);

        actividad.setNombre(datos.getNombre());
        actividad.setFechaHora(datos.getFechaHora());
        actividad.setCupo(datos.getCupo());

        Actividad modificada = actividadRepository.save(actividad);
        calcularLugaresDisponibles(modificada);

        return modificada;
    }

    public void eliminar(Long id) {
        Actividad actividad = buscarPorId(id);
        actividadRepository.delete(actividad);
    }

    private void calcularLugaresDisponibles(Actividad actividad) {
        long ocupados = inscripcionRepository
                .countByActividadIdAndEstadoNot(
                        actividad.getId(),
                        Estado.CANCELADA
                );

        actividad.setLugaresDisponibles(
                Math.max(0, actividad.getCupo() - (int) ocupados)
        );
    }

    public List<Actividad> listarPorFecha(LocalDate fecha) {
        List<Actividad> actividades = actividadRepository.findByFechaHoraBetween(
                fecha.atStartOfDay(),
                fecha.atTime(LocalTime.MAX)
        );

        actividades.forEach(this::calcularLugaresDisponibles);

        return actividades;
    }
}
