package ar.edu.undec.integrador.controller;

import ar.edu.undec.integrador.dto.SocioRequest;
import ar.edu.undec.integrador.model.Socio;
import ar.edu.undec.integrador.service.SocioService;

import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/socios")
public class SocioController {

    private final SocioService service;

    public SocioController(SocioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Socio> crear(
            @Valid @RequestBody SocioRequest request) {

        Socio creado = service.crear(request);

        return ResponseEntity
                .created(URI.create("/api/socios/" + creado.getId()))
                .body(creado);
    }

    @GetMapping
    public ResponseEntity<List<Socio>> listar() {
        return ResponseEntity.ok(service.listar());
    }
}