package siga;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Clase que gestiona la academia: alumnos, packs, compras y newsletter.
 * En este prototipo los datos se guardan en memoria con ArrayList.
 * En la próxima entrega se guardarán en la base de datos MySQL.
 */
public class GestorAcademia {

    private ArrayList<Alumno> alumnos;
    private ArrayList<Pack> packs;
    private ArrayList<EdicionNewsletter> ediciones;
    private ArrayList<Envio> historialEnvios;
    private Queue<Envio> colaEnvios;      // cola FIFO de envíos pendientes
    private LocalDate hoy;

    public GestorAcademia() {
        alumnos = new ArrayList<>();
        packs = new ArrayList<>();
        ediciones = new ArrayList<>();
        historialEnvios = new ArrayList<>();
        colaEnvios = new LinkedList<>();
        hoy = LocalDate.now();
    }

    // ------------------------------------------------------------ ALUMNOS

    /** CU01: registra un alumno validando sus datos. */
    public Alumno registrarAlumno(String dni, String nombre, String apellido,
                                  String email, String telefono) throws DatoInvalidoException {
        if (nombre.isEmpty() || apellido.isEmpty()) {
            throw new DatoInvalidoException("El nombre y el apellido son obligatorios.");
        }
        if (!dni.matches("\\d{7,8}")) {
            throw new DatoInvalidoException("El DNI debe tener 7 u 8 números, sin puntos.");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new DatoInvalidoException("El email no es válido.");
        }
        if (buscarAlumnoPorDni(dni) != null) {
            throw new DatoInvalidoException("Ya existe un alumno con el DNI " + dni + ".");
        }
        Alumno nuevo = new Alumno(dni, nombre, apellido, email, telefono, hoy);
        alumnos.add(nuevo);
        return nuevo;
    }

    /** Búsqueda secuencial: recorre la lista hasta encontrar el DNI. */
    public Alumno buscarAlumnoPorDni(String dni) {
        for (int i = 0; i < alumnos.size(); i++) {
            if (alumnos.get(i).getDni().equals(dni)) {
                return alumnos.get(i);
            }
        }
        return null;   // no se encontró
    }

    /** CU06: baja lógica del alumno (se conserva su historial). */
    public void darDeBaja(String dni) throws DatoInvalidoException {
        Alumno alumno = buscarAlumnoPorDni(dni);
        if (alumno == null) {
            throw new DatoInvalidoException("No existe un alumno con el DNI " + dni + ".");
        }
        if (!alumno.isActivo()) {
            throw new DatoInvalidoException("El alumno ya estaba dado de baja.");
        }
        alumno.setActivo(false);
    }

    /** Devuelve una copia de la lista ordenada por apellido (método burbuja). */
    public ArrayList<Alumno> listarAlumnosOrdenados() {
        ArrayList<Alumno> lista = new ArrayList<>(alumnos);
        for (int i = 0; i < lista.size() - 1; i++) {
            for (int j = 0; j < lista.size() - 1 - i; j++) {
                String a = lista.get(j).getNombreCompleto();
                String b = lista.get(j + 1).getNombreCompleto();
                if (a.compareToIgnoreCase(b) > 0) {
                    Alumno aux = lista.get(j);       // intercambio
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, aux);
                }
            }
        }
        return lista;
    }

    // ------------------------------------------------------------ COMPRAS

    public Pack buscarPack(int numero) throws DatoInvalidoException {
        if (numero < 1 || numero > packs.size()) {
            throw new DatoInvalidoException("El pack elegido no existe.");
        }
        return packs.get(numero - 1);
    }

    /** Devuelve la compra vigente del alumno para ese pack (o null). */
    public Compra consultarAccesoVigente(Alumno alumno, Pack pack) {
        return alumno.buscarAccesoVigente(pack, hoy);
    }

    /**
     * CU02: registra la compra. Si el alumno ya tiene acceso vigente y elige
     * extenderlo, el nuevo período empieza en el vencimiento actual y se aplica
     * el descuento de renovación del pack.
     */
    public Compra registrarCompra(Alumno alumno, Pack pack, boolean extender) throws DatoInvalidoException {
        if (!alumno.isActivo()) {
            throw new DatoInvalidoException("El alumno está dado de baja.");
        }
        Compra vigente = consultarAccesoVigente(alumno, pack);
        Compra compra;
        if (vigente != null && extender) {
            compra = new Compra(alumno, pack, hoy, pack.calcularPrecioRenovacion(),
                    vigente.getFechaVencimiento(), true);
        } else {
            compra = new Compra(alumno, pack, hoy, pack.getPrecio(), hoy, false);
        }
        alumno.agregarCompra(compra);
        return compra;
    }

    // ------------------------------------------------------------ VENCIMIENTOS

    /**
     * CU03: compras de alumnos activos que vencen dentro de 'dias' días,
     * ordenadas por fecha de vencimiento (ordenamiento por inserción).
     */
    public ArrayList<Compra> listarProximosAVencer(int dias) throws DatoInvalidoException {
        if (dias < 1 || dias > 365) {
            throw new DatoInvalidoException("La anticipación debe estar entre 1 y 365 días.");
        }
        ArrayList<Compra> resultado = new ArrayList<>();
        for (Alumno a : alumnos) {
            if (a.isActivo()) {
                for (Compra c : a.getCompras()) {
                    // Solo se tiene en cuenta el último acceso de cada pack:
                    // si el alumno ya renovó, la compra anterior no es una alerta.
                    boolean esUltimoAcceso = (a.buscarAccesoVigente(c.getPack(), hoy) == c);
                    if (esUltimoAcceso && c.getEstado(hoy, dias).equals("POR VENCER")) {
                        resultado.add(c);
                    }
                }
            }
        }
        ordenarPorVencimiento(resultado);
        return resultado;
    }

    /** Ordenamiento por inserción según la fecha de vencimiento. */
    private void ordenarPorVencimiento(ArrayList<Compra> lista) {
        for (int i = 1; i < lista.size(); i++) {
            Compra actual = lista.get(i);
            int j = i - 1;
            while (j >= 0 && lista.get(j).getFechaVencimiento().isAfter(actual.getFechaVencimiento())) {
                lista.set(j + 1, lista.get(j));
                j--;
            }
            lista.set(j + 1, actual);
        }
    }

    // ------------------------------------------------------------ NEWSLETTER

    /**
     * CU04 + CU07: registra la edición y pone en la cola de envíos solo a los
     * alumnos activos con acceso vigente a alguno de los packs de destino.
     * @return cantidad de alumnos que recibirán la edición
     */
    public int registrarEdicion(String titulo, String resumen, ArrayList<Pack> packsDestino)
            throws DatoInvalidoException {
        if (titulo.isEmpty()) {
            throw new DatoInvalidoException("El título es obligatorio.");
        }
        if (packsDestino.isEmpty()) {
            throw new DatoInvalidoException("Debe elegir al menos un pack.");
        }
        EdicionNewsletter edicion = new EdicionNewsletter(ediciones.size() + 1, titulo, resumen, hoy);
        for (Pack p : packsDestino) {
            edicion.agregarPackDestino(p);
        }
        ArrayList<Alumno> destinatarios = validarPackDestinatario(edicion);
        if (destinatarios.isEmpty()) {
            throw new DatoInvalidoException("No hay alumnos con acceso vigente en los packs elegidos. "
                    + "La edición no se registró.");
        }
        ediciones.add(edicion);
        for (Alumno a : destinatarios) {
            colaEnvios.add(new Envio(edicion, a));   // se encola
        }
        return destinatarios.size();
    }

    /** CU07: alumnos activos con acceso vigente a algún pack de la edición. */
    private ArrayList<Alumno> validarPackDestinatario(EdicionNewsletter edicion) {
        ArrayList<Alumno> destinatarios = new ArrayList<>();
        for (Alumno a : alumnos) {
            if (!a.isActivo()) {
                continue;
            }
            for (Pack p : edicion.getPacksDestino()) {
                if (a.buscarAccesoVigente(p, hoy) != null && !destinatarios.contains(a)) {
                    destinatarios.add(a);
                }
            }
        }
        return destinatarios;
    }

    /** Procesa la cola de envíos en orden de llegada (FIFO) y los pasa al historial. */
    public int procesarColaDeEnvios() {
        int procesados = 0;
        while (!colaEnvios.isEmpty()) {
            Envio envio = colaEnvios.poll();          // saca el primero de la cola
            envio.marcarEnviado(LocalDateTime.now());
            historialEnvios.add(envio);
            System.out.println("  Enviado: \"" + envio.getEdicion().getTitulo() + "\" a "
                    + envio.getAlumno().getEmail());
            procesados++;
        }
        return procesados;
    }

    // ------------------------------------------------------------ DATOS DE PRUEBA

    /** Carga los mismos datos que el script 02_insercion_datos.sql. */
    public void cargarDatosDePrueba() {
        packs.add(new PackFundamentos(90000));
        packs.add(new PackAvanzado(180000));
        packs.add(new PackPremium(320000));

        Alumno lucia = new Alumno("38111222", "Lucía", "Fernández", "lucia.fernandez@mail.com", "3515551001", hoy.minusDays(340));
        Alumno martin = new Alumno("40222333", "Martín", "Gómez", "martin.gomez@mail.com", "3515551002", hoy.minusDays(200));
        Alumno sofia = new Alumno("35333444", "Sofía", "Ramírez", "sofia.ramirez@mail.com", "1155551003", hoy.minusDays(400));
        Alumno tomas = new Alumno("42444555", "Tomás", "Herrera", "tomas.herrera@mail.com", "2615551004", hoy.minusDays(355));
        Alumno valentina = new Alumno("37555666", "Valentina", "López", "valentina.lopez@mail.com", "3415551005", hoy.minusDays(30));
        alumnos.add(lucia);
        alumnos.add(martin);
        alumnos.add(sofia);
        alumnos.add(tomas);
        alumnos.add(valentina);

        agregarCompraInicial(lucia, packs.get(1), 340);
        agregarCompraInicial(martin, packs.get(2), 200);
        agregarCompraInicial(sofia, packs.get(0), 400);
        agregarCompraInicial(tomas, packs.get(1), 355);
        agregarCompraInicial(valentina, packs.get(0), 30);

        EdicionNewsletter ed1 = new EdicionNewsletter(1, "Panorama semanal de mercado",
                "Resumen de tasas, dólar y Merval.", hoy.minusDays(7));
        ed1.agregarPackDestino(packs.get(0));
        ed1.agregarPackDestino(packs.get(1));
        ed1.agregarPackDestino(packs.get(2));
        ediciones.add(ed1);
        Alumno[] receptores = {lucia, martin, tomas, valentina};
        for (Alumno r : receptores) {
            Envio e = new Envio(ed1, r);
            e.marcarEnviado(hoy.minusDays(7).atTime(10, 0));
            historialEnvios.add(e);
        }
    }

    private void agregarCompraInicial(Alumno alumno, Pack pack, int diasAtras) {
        LocalDate fechaPago = hoy.minusDays(diasAtras);
        alumno.agregarCompra(new Compra(alumno, pack, fechaPago, pack.getPrecio(), fechaPago, false));
    }

    // ------------------------------------------------------------ GETTERS

    public ArrayList<Pack> getPacks() { return packs; }
    public ArrayList<Envio> getHistorialEnvios() { return historialEnvios; }
    public int getEnviosPendientes() { return colaEnvios.size(); }
    public LocalDate getHoy() { return hoy; }
}
