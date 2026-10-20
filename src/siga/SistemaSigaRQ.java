package siga;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

/**
 * Clase principal del prototipo SIGA-RQ. Muestra el menú y llama al gestor.
 */
public class SistemaSigaRQ {

    private static final Locale AR = Locale.forLanguageTag("es-AR");
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static Scanner teclado = new Scanner(System.in);
    private static GestorAcademia gestor = new GestorAcademia();

    public static void main(String[] args) {
        gestor.cargarDatosDePrueba();
        Administrador admin = new Administrador("30111000", "Ivan", "Roman", "admin@rquants.com", "admin123");

        if (!iniciarSesion(admin)) {
            System.out.println("Se superó la cantidad de intentos. El programa se cierra.");
            return;
        }

        int opcion = -1;
        do {
            mostrarMenu();
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();   // limpia el salto de línea
                switch (opcion) {
                    case 1: registrarAlumno(); break;
                    case 2: listarAlumnos(); break;
                    case 3: buscarAlumno(); break;
                    case 4: registrarCompra(); break;
                    case 5: consultarVencimientos(); break;
                    case 6: registrarEdicion(); break;
                    case 7: procesarEnvios(); break;
                    case 8: verHistorial(); break;
                    case 9: darDeBaja(); break;
                    case 0: System.out.println("¡Hasta luego!"); break;
                    default: System.out.println("Opción inválida. Elija un número del 0 al 9.");
                }
            } catch (InputMismatchException e) {
                // el usuario escribió letras en lugar de un número
                System.out.println("Error: debe ingresar un número.");
                teclado.nextLine();
            } catch (DatoInvalidoException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static boolean iniciarSesion(Administrador admin) {
        System.out.println("=== SIGA-RQ - Inicio de sesión ===");
        int intentos = 0;
        while (intentos < 3) {
            System.out.print("Contraseña: ");
            String clave = teclado.nextLine();
            if (admin.verificarClave(clave)) {
                System.out.print("Bienvenido. ");
                admin.mostrarDatos();
                return true;
            }
            intentos++;
            System.out.println("Contraseña incorrecta (" + intentos + " de 3).");
        }
        return false;
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("========== MENÚ PRINCIPAL - SIGA-RQ ==========");
        System.out.println(" 1. Registrar alumno");
        System.out.println(" 2. Listar alumnos (orden alfabético)");
        System.out.println(" 3. Buscar alumno por DNI");
        System.out.println(" 4. Registrar compra de pack");
        System.out.println(" 5. Consultar accesos próximos a vencer");
        System.out.println(" 6. Registrar edición de newsletter");
        System.out.println(" 7. Procesar cola de envíos (" + gestor.getEnviosPendientes() + " pendientes)");
        System.out.println(" 8. Ver historial de envíos");
        System.out.println(" 9. Dar de baja alumno");
        System.out.println(" 0. Salir");
        System.out.print("Elija una opción: ");
    }

    private static void registrarAlumno() throws DatoInvalidoException {
        System.out.print("DNI: ");
        String dni = teclado.nextLine().trim();
        System.out.print("Nombre: ");
        String nombre = teclado.nextLine().trim();
        System.out.print("Apellido: ");
        String apellido = teclado.nextLine().trim();
        System.out.print("Email: ");
        String email = teclado.nextLine().trim();
        System.out.print("Teléfono: ");
        String telefono = teclado.nextLine().trim();
        Alumno a = gestor.registrarAlumno(dni, nombre, apellido, email, telefono);
        System.out.println("Alumno registrado correctamente: " + a.getNombreCompleto());
    }

    private static void listarAlumnos() {
        System.out.println("--- Alumnos ---");
        for (Alumno a : gestor.listarAlumnosOrdenados()) {
            a.mostrarDatos();   // llamada polimórfica
        }
    }

    private static void buscarAlumno() {
        System.out.print("DNI a buscar: ");
        String dni = teclado.nextLine().trim();
        Alumno a = gestor.buscarAlumnoPorDni(dni);
        if (a == null) {
            System.out.println("No se encontró un alumno con ese DNI.");
        } else {
            a.mostrarDatos();
            for (Compra c : a.getCompras()) {
                System.out.println("   Pack " + c.getPack().getNombre() + " - vence el "
                        + c.getFechaVencimiento().format(FORMATO) + " - "
                        + c.getEstado(gestor.getHoy(), 30));
            }
        }
    }

    private static void registrarCompra() throws DatoInvalidoException {
        System.out.print("DNI del alumno: ");
        Alumno alumno = gestor.buscarAlumnoPorDni(teclado.nextLine().trim());
        if (alumno == null) {
            throw new DatoInvalidoException("El alumno no existe.");
        }
        mostrarPacks();
        System.out.print("Número de pack: ");
        Pack pack = gestor.buscarPack(teclado.nextInt());
        teclado.nextLine();

        boolean extender = false;
        Compra vigente = gestor.consultarAccesoVigente(alumno, pack);
        if (vigente != null) {
            System.out.println("El alumno ya tiene acceso vigente a " + pack.getNombre() + " hasta el "
                    + vigente.getFechaVencimiento().format(FORMATO) + ".");
            System.out.printf(AR, "Si renueva ahora paga $%,.2f (%.0f %% de descuento).%n",
                    pack.calcularPrecioRenovacion(), pack.getPorcentajeDescuentoRenovacion());
            System.out.print("¿Extender 365 días desde ese vencimiento? (S/N): ");
            extender = teclado.nextLine().trim().equalsIgnoreCase("S");
        }
        Compra compra = gestor.registrarCompra(alumno, pack, extender);
        System.out.printf(AR, "Compra registrada. Monto: $%,.2f. Acceso hasta el %s.%n",
                compra.getMonto(), compra.getFechaVencimiento().format(FORMATO));
    }

    private static void consultarVencimientos() throws DatoInvalidoException {
        System.out.print("¿Con cuántos días de anticipación? ");
        int dias = teclado.nextInt();
        teclado.nextLine();
        ArrayList<Compra> lista = gestor.listarProximosAVencer(dias);
        if (lista.isEmpty()) {
            System.out.println("No hay accesos por vencer en ese plazo.");
            return;
        }
        System.out.println("--- Accesos que vencen en los próximos " + dias + " días ---");
        for (Compra c : lista) {
            System.out.println(c.getAlumno().getNombreCompleto() + " | " + c.getPack().getNombre()
                    + " | vence " + c.getFechaVencimiento().format(FORMATO)
                    + " | faltan " + c.getDiasRestantes(gestor.getHoy()) + " días");
        }
    }

    private static void registrarEdicion() throws DatoInvalidoException {
        System.out.print("Título: ");
        String titulo = teclado.nextLine().trim();
        System.out.print("Resumen: ");
        String resumen = teclado.nextLine().trim();
        mostrarPacks();
        System.out.print("Números de pack destino separados por coma (ej: 1,2): ");
        String[] partes = teclado.nextLine().split(",");
        ArrayList<Pack> destino = new ArrayList<>();
        for (String parte : partes) {
            try {
                destino.add(gestor.buscarPack(Integer.parseInt(parte.trim())));
            } catch (NumberFormatException e) {
                throw new DatoInvalidoException("\"" + parte.trim() + "\" no es un número de pack.");
            }
        }
        int cantidad = gestor.registrarEdicion(titulo, resumen, destino);
        System.out.println("Edición registrada. " + cantidad + " envío(s) agregados a la cola.");
    }

    private static void procesarEnvios() {
        int n = gestor.procesarColaDeEnvios();
        System.out.println(n == 0 ? "No hay envíos pendientes." : "Se procesaron " + n + " envío(s).");
    }

    private static void verHistorial() {
        System.out.println("--- Historial de envíos ---");
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Envio e : gestor.getHistorialEnvios()) {
            System.out.println("Ed. " + e.getEdicion().getNumero() + " \"" + e.getEdicion().getTitulo()
                    + "\" -> " + e.getAlumno().getNombreCompleto() + " (" + e.getFechaEnvio().format(f) + ")");
        }
    }

    private static void darDeBaja() throws DatoInvalidoException {
        System.out.print("DNI del alumno a dar de baja: ");
        String dni = teclado.nextLine().trim();
        gestor.darDeBaja(dni);
        System.out.println("El alumno fue dado de baja.");
    }

    private static void mostrarPacks() {
        ArrayList<Pack> packs = gestor.getPacks();
        for (int i = 0; i < packs.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + packs.get(i));
        }
    }
}
