package ar.edu.undec.integrador.service;

import ar.edu.undec.integrador.dto.SocioRequest;
import ar.edu.undec.integrador.model.Socio;
import ar.edu.undec.integrador.repository.SocioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocioService {

    private final SocioRepository repository;

    public SocioService(SocioRepository repository) {
        this.repository = repository;
    }

    public List<Socio> listar() {
        return repository.findAll();
    }

    public Socio crear(SocioRequest request) {
        Socio socio = new Socio();
        socio.setNombre(request.getNombre());
        socio.setEmail(request.getEmail());
        return repository.save(socio);
    }
}