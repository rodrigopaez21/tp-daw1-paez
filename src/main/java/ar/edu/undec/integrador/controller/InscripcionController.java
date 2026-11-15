package ar.edu.undec.integrador.controller;

import ar.edu.undec.integrador.dto.*;
import ar.edu.undec.integrador.model.Inscripcion;
import ar.edu.undec.integrador.service.InscripcionService;

import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    private final InscripcionService service;

    public InscripcionController(InscripcionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Inscripcion> crear(
            @Valid @RequestBody InscripcionRequest request) {

        Inscripcion creada = service.crear(request);

        return ResponseEntity
                .created(URI.create("/api/inscripciones/" + creada.getId()))
                .body(creada);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Inscripcion> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoRequest request) {

        return ResponseEntity.ok(
                service.cambiarEstado(id, request.getEstado())
        );
    }
}