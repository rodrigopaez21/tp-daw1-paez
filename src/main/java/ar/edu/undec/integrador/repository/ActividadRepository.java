package ar.edu.undec.integrador.repository;

import ar.edu.undec.integrador.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ActividadRepository
        extends JpaRepository<Actividad, Long> {

    List<Actividad> findByFechaHoraBetween(
            LocalDateTime inicio,
            LocalDateTime fin
    );
}