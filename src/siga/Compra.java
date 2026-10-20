package siga;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Compra de un pack (pago único). Da acceso durante 365 días.
 */
public class Compra {

    public static final int DIAS_DE_ACCESO = 365;

    private Alumno alumno;
    private Pack pack;
    private LocalDate fechaPago;
    private double monto;
    private LocalDate fechaVencimiento;
    private boolean renovacion;

    /**
     * @param fechaInicio desde cuándo corre el acceso (la fecha de pago, o el
     *                    vencimiento anterior si es una renovación anticipada)
     */
    public Compra(Alumno alumno, Pack pack, LocalDate fechaPago, double monto,
                  LocalDate fechaInicio, boolean renovacion) {
        this.alumno = alumno;
        this.pack = pack;
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.renovacion = renovacion;
        this.fechaVencimiento = fechaInicio.plusDays(DIAS_DE_ACCESO);   // RF03
    }

    public boolean estaVigente(LocalDate hoy) {
        return !fechaVencimiento.isBefore(hoy);
    }

    public long getDiasRestantes(LocalDate hoy) {
        return ChronoUnit.DAYS.between(hoy, fechaVencimiento);
    }

    /** Estado del acceso según los días que faltan (ver caso de prueba CPU05). */
    public String getEstado(LocalDate hoy, int diasAnticipacion) {
        long dias = getDiasRestantes(hoy);
        if (dias < 0) {
            return "VENCIDO";
        } else if (dias <= diasAnticipacion) {
            return "POR VENCER";
        } else {
            return "VIGENTE";
        }
    }

    public Alumno getAlumno() { return alumno; }
    public Pack getPack() { return pack; }
    public LocalDate getFechaPago() { return fechaPago; }
    public double getMonto() { return monto; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public boolean isRenovacion() { return renovacion; }
}
