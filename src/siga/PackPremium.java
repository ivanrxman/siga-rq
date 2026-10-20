package siga;

public class PackPremium extends Pack {

    public PackPremium(double precio) {
        super("Premium", precio);
    }

    @Override
    public int getNivelNewsletter() {
        return 3;   // newsletter completa
    }

    @Override
    public double getPorcentajeDescuentoRenovacion() {
        return 20;
    }
}
