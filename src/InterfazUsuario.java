import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Interfaz ASCII del primer avance.
 * Autores: [Completar nombres del equipo]
 */
public class InterfazUsuario {
    private static final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Scanner sc = new Scanner(System.in);
    private InstitutoMeteorologia instituto;

    public static void main(String[] args) {
        new InterfazUsuario().menuPrincipal();
    }

    private void menuPrincipal() {
        instituto = new InstitutoMeteorologia();
        int opcion;
        do {
            System.out.println("\nSISTEMA DE INFORMACIÓN METEOROLÓGICA");
            System.out.println("1. Crear región");
            System.out.println("2. Crear comuna");
            System.out.println("3. Crear estación meteorológica");
            System.out.println("4. Instalar sensor");
            System.out.println("5. Registrar medición");
            System.out.println("6. Generar listados");
            System.out.println("7. Salir");
            opcion = leerOpcion("Opción: ", 1, 7);
            switch (opcion) {
                case 1: crearRegion(); break;
                case 2: crearComuna(); break;
                case 3: crearEstacionMeteorologica(); break;
                case 4: instalarSensor(); break;
                case 5: registrarMedicion(); break;
                case 6: menuListados(); break;
                default: System.out.println("Programa finalizado.");
            }
        } while (opcion != 7);
    }

    private void crearRegion() {
        System.out.println("\nCREAR REGIÓN");
        int codigo = leerEntero("Código: ");
        String nombre = leerTexto("Nombre: ");
        informar(instituto.creaRegion(codigo, nombre),
                "Región creada correctamente.",
                "No fue posible crear la región: código o nombre repetido.");
    }

    private void crearComuna() {
        System.out.println("\nCREAR COMUNA");
        int codigoRegion = leerEntero("Código de región: ");
        int codigo = leerEntero("Código de comuna: ");
        String nombre = leerTexto("Nombre: ");
        informar(instituto.creaComuna(codigo, nombre, codigoRegion),
                "Comuna creada correctamente.",
                "No fue posible crear la comuna: región inexistente o datos repetidos.");
    }

    private void crearEstacionMeteorologica() {
        System.out.println("\nCREAR ESTACIÓN METEOROLÓGICA");
        String codigo = leerTexto("Código de estación: ");
        String nombre = leerTexto("Nombre: ");
        float longitud = leerFloat("Longitud: ");
        float latitud = leerFloat("Latitud: ");
        float altitud = leerFloat("Altitud (m): ");
        int codRegion = leerEntero("Código de región: ");
        int codComuna = leerEntero("Código de comuna: ");
        informar(instituto.creaEstacion(codigo, nombre, longitud, latitud,
                        altitud, codRegion, codComuna),
                "Estación meteorológica creada correctamente.",
                "No fue posible crear la estación: revise código, región y comuna.");
    }

    private void instalarSensor() {
        System.out.println("\nINSTALAR SENSOR");
        String codEstacion = leerTexto("Código de estación: ");
        int opcionTipo = leerOpcion(
                "Tipo [1 Temp.  2 Hum.  3 Presión  4 Viento  5 Precip.]: ", 1, 5);
        TipoSensor tipo = tipoDesdeOpcion(opcionTipo);
        String codigo = leerTexto("Código de sensor: ");
        String marca = leerTexto("Marca: ");
        String modelo = leerTexto("Modelo: ");
        informar(instituto.instalaSensor(codigo, marca, modelo, tipo, codEstacion),
                "Sensor de " + tipo.name().toLowerCase() + " instalado correctamente.",
                "No fue posible instalar el sensor: revise estación, estado, código y tipo.");
    }

    private void registrarMedicion() {
        System.out.println("\nREGISTRAR MEDICIÓN");
        String codEstacion = leerTexto("Código de estación: ");
        String codSensor = leerTexto("Código de sensor: ");
        LocalDateTime fechaHora = LocalDateTime.parse(
                leerTexto("Fecha y hora [dd/MM/yyyy HH:mm]: "), FORMATO_FECHA_HORA);
        float valor = leerFloat("Valor: ");
        informar(instituto.registraMedicion(fechaHora, valor, codEstacion, codSensor),
                "Medición registrada correctamente.",
                "No fue posible registrar la medición: revise estados, fecha y valor.");
    }

    private void menuListados() {
        int opcion;
        do {
            System.out.println("\nGENERAR LISTADOS");
            System.out.println("1. Regiones");
            System.out.println("2. Comunas");
            System.out.println("3. Estaciones de una comuna");
            System.out.println("4. Sensores de una estación");
            System.out.println("5. Mediciones de un sensor");
            System.out.println("6. Volver al menú principal");
            opcion = leerOpcion("Opción: ", 1, 6);
            switch (opcion) {
                case 1: listarRegiones(); break;
                case 2: listarComunas(); break;
                case 3: listarEstaciones(); break;
                case 4: listarSensores(); break;
                case 5: listarMediciones(); break;
                default: break;
            }
        } while (opcion != 6);
    }

    private void listarRegiones() {
        mostrarTabla("REGIONES",
                new String[]{"CÓDIGO", "NOMBRE", "COMUNAS", "ESTACIONES"},
                instituto.listaRegiones());
    }

    private void listarComunas() {
        mostrarTabla("COMUNAS",
                new String[]{"CÓDIGO", "NOMBRE", "REGIÓN", "ESTACIONES", "ACTIVAS"},
                instituto.listaComunas());
    }

    private void listarEstaciones() {
        int region = leerEntero("Código de región: ");
        int comuna = leerEntero("Código de comuna: ");
        mostrarTabla("ESTACIONES DE LA COMUNA",
                new String[]{"CÓDIGO", "NOMBRE", "UBICACIÓN", "ESTADO", "SENSORES OPERATIVOS"},
                instituto.listaEstaciones(region, comuna));
    }

    private void listarSensores() {
        String estacion = leerTexto("Código de estación: ");
        mostrarTabla("SENSORES DE " + estacion.toUpperCase(),
                new String[]{"CÓDIGO", "TIPO", "MARCA", "MODELO", "UNIDAD", "ESTADO", "ÚLTIMA MEDICIÓN"},
                instituto.listaSensores(estacion));
    }

    private void listarMediciones() {
        String estacion = leerTexto("Código de estación: ");
        String sensor = leerTexto("Código de sensor: ");
        LocalDateTime inicio = LocalDateTime.parse(
                leerTexto("Inicio [dd/MM/yyyy HH:mm]: "), FORMATO_FECHA_HORA);
        LocalDateTime fin = LocalDateTime.parse(
                leerTexto("Fin [dd/MM/yyyy HH:mm]: "), FORMATO_FECHA_HORA);
        mostrarTabla("MEDICIONES DEL SENSOR " + sensor.toUpperCase(),
                new String[]{"FECHA", "HORA", "VALOR", "UNIDAD"},
                instituto.listaMediciones(estacion, sensor, inicio, fin));
    }

    private int leerOpcion(String mensaje, int minimo, int maximo) {
        int opcion;
        do {
            opcion = leerEntero(mensaje);
            if (opcion < minimo || opcion > maximo) {
                System.out.println("> Opción fuera de rango. Intente nuevamente.");
            }
        } while (opcion < minimo || opcion > maximo);
        return opcion;
    }

    private int leerEntero(String mensaje) {
        System.out.print(mensaje);
        return Integer.parseInt(sc.nextLine().trim());
    }

    private float leerFloat(String mensaje) {
        System.out.print(mensaje);
        return Float.parseFloat(sc.nextLine().trim().replace(',', '.'));
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private TipoSensor tipoDesdeOpcion(int opcion) {
        switch (opcion) {
            case 1: return TipoSensor.TEMPERATURA;
            case 2: return TipoSensor.HUMEDAD;
            case 3: return TipoSensor.PRESION;
            case 4: return TipoSensor.VIENTO;
            default: return TipoSensor.PRECIPITACION;
        }
    }

    private void informar(boolean exito, String mensajeExito, String mensajeError) {
        System.out.println("> " + (exito ? mensajeExito : mensajeError));
    }

    private void mostrarTabla(String titulo, String[] encabezados, String[][] datos) {
        System.out.println("\n" + titulo);
        if (datos.length == 0) {
            System.out.println("> No existen datos para el criterio indicado.");
            return;
        }
        int[] anchos = new int[encabezados.length];
        for (int c = 0; c < encabezados.length; c++) {
            anchos[c] = encabezados[c].length();
            for (String[] fila : datos) {
                anchos[c] = Math.max(anchos[c], fila[c].length());
            }
        }
        imprimirFila(encabezados, anchos);
        for (String[] fila : datos) {
            imprimirFila(fila, anchos);
        }
    }

    private void imprimirFila(String[] fila, int[] anchos) {
        StringBuilder linea = new StringBuilder();
        for (int i = 0; i < fila.length; i++) {
            linea.append(String.format("%-" + (anchos[i] + 3) + "s", fila[i]));
        }
        System.out.println(linea.toString().stripTrailing());
    }
}
