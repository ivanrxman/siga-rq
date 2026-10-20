package siga;

public class PackFundamentos extends Pack {

    public PackFundamentos(double precio) {
        super("Fundamentos", precio);
    }

    @Override
    public int getNivelNewsletter() {
        return 1;   // newsletter básica
    }

    @Override
    public double getPorcentajeDescuentoRenovacion() {
        return 10;  // 10 % de descuento al renovar
    }
}
