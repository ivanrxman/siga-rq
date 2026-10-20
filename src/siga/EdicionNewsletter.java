package siga;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Edición de la newsletter dirigida a uno o más packs.
 */
public class EdicionNewsletter {

    private int numero;
    private String titulo;
    private String resumen;
    private LocalDate fechaPublicacion;
    private ArrayList<Pack> packsDestino;

    public EdicionNewsletter(int numero, String titulo, String resumen, LocalDate fechaPublicacion) {
        this.numero = numero;
        this.titulo = titulo;
        this.resumen = resumen;
        this.fechaPublicacion = fechaPublicacion;
        this.packsDestino = new ArrayList<>();
    }

    public void agregarPackDestino(Pack pack) {
        if (!packsDestino.contains(pack)) {
            packsDestino.add(pack);
        }
    }

    public int getNumero() { return numero; }
    public String getTitulo() { return titulo; }
    public String getResumen() { return resumen; }
    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public ArrayList<Pack> getPacksDestino() { return packsDestino; }
}
