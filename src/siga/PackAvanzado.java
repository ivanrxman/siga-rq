package siga;

public class PackAvanzado extends Pack {

    public PackAvanzado(double precio) {
        super("Avanzado", precio);
    }

    @Override
    public int getNivelNewsletter() {
        return 2;   // newsletter intermedia
    }

    @Override
    public double getPorcentajeDescuentoRenovacion() {
        return 15;
    }
}
