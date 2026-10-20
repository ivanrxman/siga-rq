package siga;

import java.time.LocalDateTime;

/**
 * Registro de que una edición se envió a un alumno (trazabilidad, RF07).
 */
public class Envio {

    private EdicionNewsletter edicion;
    private Alumno alumno;
    private LocalDateTime fechaEnvio;

    public Envio(EdicionNewsletter edicion, Alumno alumno) {
        this.edicion = edicion;
        this.alumno = alumno;
        this.fechaEnvio = null;   // se completa cuando se procesa la cola
    }

    public void marcarEnviado(LocalDateTime fecha) {
        this.fechaEnvio = fecha;
    }

    public EdicionNewsletter getEdicion() { return edicion; }
    public Alumno getAlumno() { return alumno; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
}
