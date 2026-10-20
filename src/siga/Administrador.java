package siga;

/**
 * Administrador de R Quants: es quien usa el sistema.
 */
public class Administrador extends Persona {

    private String clave;

    public Administrador(String dni, String nombre, String apellido, String email, String clave) {
        super(dni, nombre, apellido, email);
        this.clave = clave;
    }

    public boolean verificarClave(String claveIngresada) {
        return clave.equals(claveIngresada);
    }

    @Override
    public void mostrarDatos() {
        System.out.println("Administrador: " + getNombreCompleto() + " | Email: " + getEmail());
    }
}
