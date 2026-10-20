package siga;

/**
 * Excepción propia del sistema: se lanza cuando un dato ingresado
 * no cumple una regla de negocio (DNI inválido, alumno inexistente, etc.).
 */
public class DatoInvalidoException extends Exception {

    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
