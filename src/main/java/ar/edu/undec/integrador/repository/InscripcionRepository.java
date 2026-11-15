package ar.edu.undec.integrador.repository;

import ar.edu.undec.integrador.model.Inscripcion;
import ar.edu.undec.integrador.model.Inscripcion.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InscripcionRepository
        extends JpaRepository<Inscripcion, Long> {

    List<Inscripcion> findByActividadId(Long actividadId);

    boolean existsByActividadIdAndSocioIdAndEstadoNot(
            Long actividadId, Long socioId, Estado estado);

    long countByActividadIdAndEstadoNot(
            Long actividadId, Estado estado);

    boolean existsByActividadId(Long actividadId);

    @Query("""
        SELECT COUNT(i) > 0
        FROM Inscripcion i
        WHERE i.actividad.id = :actividadId
          AND i.estado <> :estado
        """)
    boolean tieneInscripcionesActivas(
            @Param("actividadId") Long actividadId,
            @Param("estado") Estado estado
    );
}