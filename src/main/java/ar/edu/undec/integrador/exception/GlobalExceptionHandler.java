
package ar.edu.undec.integrador.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> validacion(
            MethodArgumentNotValidException ex) {

        ProblemDetail p = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        p.setTitle("Datos inválidos");
        p.setDetail("La petición tiene errores");

        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );

        p.setProperty("errores", errores);

        return ResponseEntity.badRequest().body(p);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> jsonInvalido(
            HttpMessageNotReadableException ex) {

        return error(
                HttpStatus.BAD_REQUEST,
                "JSON inválido",
                "El cuerpo de la petición no es válido"
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> tipoInvalido(
            MethodArgumentTypeMismatchException ex) {

        return error(
                HttpStatus.BAD_REQUEST,
                "Parámetro inválido",
                "El identificador o parámetro tiene un formato incorrecto"
        );
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ProblemDetail> noEncontrado(
            RecursoNoEncontradoException ex) {

        return error(
                HttpStatus.NOT_FOUND,
                "Recurso no encontrado",
                ex.getMessage()
        );
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ProblemDetail> reglaNegocio(
            ReglaNegocioException ex) {

        return error(
                HttpStatus.CONFLICT,
                "Conflicto",
                ex.getMessage()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> integridad(
            DataIntegrityViolationException ex) {

        return error(
                HttpStatus.CONFLICT,
                "Conflicto",
                "La operación viola una restricción de datos"
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> argumentoInvalido(
            IllegalArgumentException ex) {

        return error(
                HttpStatus.BAD_REQUEST,
                "Datos inválidos",
                ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> errorGeneral(Exception ex) {

        return error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno",
                "Ocurrió un error inesperado"
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemDetail> recursoNoExiste(
            NoResourceFoundException ex) {

        return error(
                HttpStatus.NOT_FOUND,
                "Recurso no encontrado",
                "La ruta solicitada no existe"
        );
    }

    private ResponseEntity<ProblemDetail> error(
            HttpStatus status,
            String titulo,
            String detalle) {

        ProblemDetail p = ProblemDetail.forStatus(status);
        p.setTitle(titulo);
        p.setDetail(detalle);

        return ResponseEntity.status(status).body(p);
    }
}