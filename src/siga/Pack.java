package siga;

import java.util.Locale;

/**
 * Clase abstracta que representa un pack de contenido on-demand.
 * Cada tipo de pack define su nivel de newsletter y su descuento de renovación.
 */
public abstract class Pack {

    protected String nombre;
    protected double precio;

    public Pack(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    // Métodos abstractos que implementa cada subclase (polimorfismo)
    public abstract int getNivelNewsletter();
    public abstract double getPorcentajeDescuentoRenovacion();

    /** Precio que paga un alumno que renueva antes de que se le venza el acceso. */
    public double calcularPrecioRenovacion() {
        return precio - (precio * getPorcentajeDescuentoRenovacion() / 100);
    }

    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }

    @Override
    public String toString() {
        return nombre + " ($" + String.format(Locale.forLanguageTag("es-AR"), "%,.2f", precio) + ")";
    }
}
