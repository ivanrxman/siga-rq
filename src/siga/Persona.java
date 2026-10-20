package siga;

/**
 * Clase abstracta que representa a una persona del sistema.
 * No se pueden crear objetos Persona directamente: se crean Alumnos o Administradores.
 */
public abstract class Persona {

    // Atributos privados (encapsulamiento): solo se acceden con getters y setters
    private String dni;
    private String nombre;
    private String apellido;
    private String email;

    // Constructor
    public Persona(String dni, String nombre, String apellido, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    // Método abstracto: cada subclase lo implementa a su manera (polimorfismo)
    public abstract void mostrarDatos();

    public String getNombreCompleto() {
        return apellido + ", " + nombre;
    }

    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}
