package ar.edu.undec.integrador.controller;

import ar.edu.undec.integrador.dto.ActividadRequest;
import ar.edu.undec.integrador.model.Actividad;
import ar.edu.undec.integrador.model.Inscripcion;
import ar.edu.undec.integrador.service.ActividadService;
import ar.edu.undec.integrador.service.InscripcionService;
import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping({"/api/actividades", "/api/actividades/"})
public class ActividadController {

    private final ActividadService actividadService;
    private final InscripcionService inscripcionService;

    public ActividadController(ActividadService actividadService,InscripcionService inscripcionService) {
        this.actividadService = actividadService;
        this.inscripcionService = inscripcionService;
    }

    @PostMapping
    public ResponseEntity<Actividad> crear(@Valid @RequestBody ActividadRequest request) {

        Actividad actividad = new Actividad();
        actividad.setNombre(request.getNombre());
        actividad.setFechaHora(request.getFechaHora());
        actividad.setCupo(request.getCupo());

        Actividad creada = actividadService.crear(actividad);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/actividades/{id}")
                .buildAndExpand(creada.getId())
                .toUri();

        return ResponseEntity.created(location).body(creada);
    }

    @GetMapping
    public ResponseEntity<List<Actividad>> listar(
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate fecha) {
            if (fecha == null) {
                return ResponseEntity.ok(actividadService.listar());
            }
        return ResponseEntity.ok(actividadService.listarPorFecha(fecha));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actividad> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(actividadService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actividad> modificar(
            @PathVariable Long id,
            @Valid @RequestBody ActividadRequest request) {

        Actividad datos = new Actividad();
        datos.setNombre(request.getNombre());
        datos.setFechaHora(request.getFechaHora());
        datos.setCupo(request.getCupo());

        return ResponseEntity.ok(actividadService.modificar(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        actividadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/inscripciones")
    public ResponseEntity<List<Inscripcion>> listarInscripciones(
        @PathVariable Long id) {
            return ResponseEntity.ok(
        inscripcionService.listarPorActividad(id)
        );
    }
}