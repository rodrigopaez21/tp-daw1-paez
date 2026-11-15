# TP Integrador · DAW1 — Sistema de gestión de gimnasio

**Alumno/a:** Paez, Rodrigo Ignacio · **Legajo:** 510023
**Enunciado elegido:** gimnasio


## Descripción

Sistema de gestión para un gimnasio que permite administrar actividades, socios e inscripciones.
La API permite consultar y gestionar las actividades, registrar socios y controlar las inscripciones, incluyendo el cupo disponible y los estados de cada inscripción.

## Cómo ejecutarlo

Requisitos: Java 21 (Maven viene incluido con `mvnw`).

```bash
./mvnw spring-boot:run
```

La API queda en `http://localhost:8080/api`. La base H2 se guarda en `data/` (no se sube al repositorio).
Consola H2: `http://localhost:8080/h2-console`.

## Modelo

| Entidad | Atributos principales | Relaciones |
|---|---|---|
| Actividad | id, nombre, fechaHora, cupo, lugaresDisponibles | Tiene muchas inscripciones |
| Socio | id, nombre, email | Tiene muchas inscripciones |
| Inscripcion | id, estado | N a 1 con Actividad y N a 1 con Socio |

Estados de Inscripcion: CONFIRMADA → CANCELADA o CONFIRMADA → ASISTIO. El estado CANCELADA es final.

## Endpoints

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | /api/actividades | Lista todas las actividades | 200 |
| GET | /api/actividades?fecha=AAAA-MM-DD | Lista actividades de una fecha | 200, 400 |
| GET | /api/actividades/{id} | Busca una actividad por id | 200, 400, 404 |
| POST | /api/actividades | Crea una actividad | 201, 400 |
| PUT | /api/actividades/{id} | Modifica una actividad | 200, 400, 404 |
| DELETE | /api/actividades/{id} | Elimina una actividad | 204, 404, 409 |
| GET | /api/actividades/{id}/inscripciones | Lista las inscripciones de una actividad | 200, 400, 404 |
| GET | /api/socios | Lista todos los socios | 200 |
| POST | /api/socios | Crea un socio | 201, 400, 409 |
| POST | /api/inscripciones | Crea una inscripción | 201, 400, 404, 409 |
| PATCH | /api/inscripciones/{id}/estado | Cambia el estado de una inscripción | 200, 400, 404, 409 |

## Reglas de negocio

1. No se permite inscribir al mismo socio más de una vez en una misma actividad si ya tiene una inscripción activa. Devuelve 409 Conflict.
2. No se permite superar el cupo de una actividad. Devuelve 409 Conflict.
3. Una inscripción cancelada libera un lugar para otra inscripción.
4. No se permite cancelar una inscripción cuando faltan menos de dos horas para el comienzo de la actividad. Devuelve 409 Conflict.
5. Una inscripción en estado CANCELADA no puede volver a otro estado. Devuelve 409 Conflict.
6. Los datos obligatorios y los formatos inválidos se rechazan con 400 Bad Request. Los recursos inexistentes devuelven 404 Not Found.

## Pruebas

En `pruebas.http` están todas las peticiones de ejemplo (se ejecutan desde IntelliJ).
