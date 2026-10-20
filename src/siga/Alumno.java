package siga;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Alumno de R Quants. Hereda de Persona y guarda sus compras.
 */
public class Alumno extends Persona {

    private String telefono;
    private LocalDate fechaAlta;
    private boolean activo;
    private ArrayList<Compra> compras;

    public Alumno(String dni, String nombre, String apellido, String email,
                  String telefono, LocalDate fechaAlta) {
        super(dni, nombre, apellido, email);   // llama al constructor de Persona
        this.telefono = telefono;
        this.fechaAlta = fechaAlta;
        this.activo = true;                    // todo alumno nuevo empieza activo
        this.compras = new ArrayList<>();
    }

    public void agregarCompra(Compra compra) {
        compras.add(compra);
    }

    /**
     * Devuelve la compra vigente más lejana para un pack, o null si no tiene acceso.
     */
    public Compra buscarAccesoVigente(Pack pack, LocalDate hoy) {
        Compra resultado = null;
        for (Compra c : compras) {
            if (c.getPack() == pack && c.estaVigente(hoy)) {
                if (resultado == null || c.getFechaVencimiento().isAfter(resultado.getFechaVencimiento())) {
                    resultado = c;
                }
            }
        }
        return resultado;
    }

    @Override
    public void mostrarDatos() {
        String estado = activo ? "Activo" : "Dado de baja";
        System.out.println("Alumno: " + getNombreCompleto() + " | DNI: " + getDni()
                + " | Email: " + getEmail() + " | Estado: " + estado);
    }

    public String getTelefono() { return telefono; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public ArrayList<Compra> getCompras() { return compras; }
}
