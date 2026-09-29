/**
 * Autor Cristobal Quezada
 */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class InterfazUsuario {

    private Scanner sc = new Scanner(System.in);
    private InstitutoMeteorologia instituto;

    public static void main(String[] args) {
        InterfazUsuario interfaz = new InterfazUsuario();
        interfaz.menuPrincipal();
    }

    private void menuPrincipal() {
        instituto = new InstitutoMeteorologia();
        int opcion;
        do {
            System.out.println();
            System.out.println("SISTEMA DE INFORMACIÓN METEOROLÓGICA");
            System.out.println("------------------------------------");
            System.out.println("1. Crear región");
            System.out.println("2. Crear comuna");
            System.out.println("3. Crear estación meteorológica");
            System.out.println("4. Instalar sensor");
            System.out.println("5. Registrar medición");
            System.out.println("6. Generar listados");
            System.out.println("7. Salir");
            System.out.print("Opción: ");
            opcion = leerOpcion(1, 7);
            switch (opcion) {
                case 1:
                    crearRegion();
                    break;
                case 2:
                    crearComuna();
                    break;
                case 3:
                    crearEstacionMeteorologica();
                    break;
                case 4:
                    instalarSensor();
                    break;
                case 5:
                    registrarMedicion();
                    break;
                case 6:
                    menuListados();
                    break;
                case 7:
                    System.out.println("Hasta luego.");
                    break;
            }
        } while (opcion != 7);
    }

    private void crearRegion() {
        System.out.println();
        System.out.println("CREAR REGIÓN");
        System.out.println("------------");
        System.out.print("Código de región: ");
        int codigo = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        boolean exito = instituto.creaRegion(codigo, nombre);
        if (exito) {
            System.out.println("> Región creada correctamente.");
        } else {
            System.out.println("> No fue posible crear la región (código o nombre repetido).");
        }
    }

    private void crearComuna() {
        System.out.println();
        System.out.println("CREAR COMUNA");
        System.out.println("------------");
        System.out.print("Código de región: ");
        int codigoRegion = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Código de comuna: ");
        int codigo = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        boolean exito = instituto.creaComuna(codigo, nombre, codigoRegion);
        if (exito) {
            System.out.println("> Comuna creada correctamente.");
        } else {
            System.out.println("> No fue posible crear la comuna (región inexistente, o código/nombre repetido).");
        }
    }

    private void crearEstacionMeteorologica() {
        System.out.println();
        System.out.println("CREAR ESTACIÓN METEOROLÓGICA");
        System.out.println("-----------------------------");
        System.out.print("Código de estación: ");
        String codigo = sc.nextLine().trim();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Longitud: ");
        float longitud = Float.parseFloat(sc.nextLine().trim());
        System.out.print("Latitud: ");
        float latitud = Float.parseFloat(sc.nextLine().trim());
        System.out.print("Altitud (m): ");
        float altitud = Float.parseFloat(sc.nextLine().trim());
        System.out.print("Código de región: ");
        int codigoRegion = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Código de comuna: ");
        int codigoComuna = Integer.parseInt(sc.nextLine().trim());
        boolean exito = instituto.creaEstacion(codigo, nombre, longitud, latitud, altitud, codigoRegion, codigoComuna);
        if (exito) {
            System.out.println("> Estación meteorológica creada correctamente.");
        } else {
            System.out.println("> No fue posible crear la estación (código repetido, o región/comuna inexistente).");
        }
    }

    private void instalarSensor() {
        System.out.println();
        System.out.println("INSTALAR SENSOR");
        System.out.println("----------------");
        System.out.print("Código de estación: ");
        String codigoEstacion = sc.nextLine().trim();
        System.out.print("Tipo [1 Temp.  2 Hum.  3 Presión  4 Viento  5 Precip.]: ");
        int opcionTipo = leerOpcion(1, 5);
        TipoSensor tipo = obtenerTipoSensor(opcionTipo);
        System.out.print("Código de sensor: ");
        String codigo = sc.nextLine().trim();
        System.out.print("Marca: ");
        String marca = sc.nextLine().trim();
        System.out.print("Modelo: ");
        String modelo = sc.nextLine().trim();
        boolean exito = instituto.instalaSensor(codigo, marca, modelo, tipo, codigoEstacion);
        if (exito) {
            System.out.println("> Sensor instalado correctamente.");
        } else {
            System.out.println("> No fue posible instalar el sensor.");
        }
    }

    private void registrarMedicion() {
        System.out.println();
        System.out.println("REGISTRAR MEDICIÓN");
        System.out.println("-------------------");
        System.out.print("Código de estación: ");
        String codigoEstacion = sc.nextLine().trim();
        System.out.print("Código de sensor: ");
        String codigoSensor = sc.nextLine().trim();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.print("Fecha y hora [dd/MM/yyyy HH:mm]: ");
        LocalDateTime fechaHora = LocalDateTime.parse(sc.nextLine().trim(), formato);
        System.out.print("Valor: ");
        float valor = Float.parseFloat(sc.nextLine().trim());
        boolean exito = instituto.registraMedicion(fechaHora, valor, codigoEstacion, codigoSensor);
        if (exito) {
            System.out.println("> Medición registrada correctamente.");
        } else {
            System.out.println("> No fue posible registrar la medición.");
        }
    }

    private void menuListados() {
        int opcion;
        do {
            System.out.println();
            System.out.println("GENERAR LISTADOS");
            System.out.println("-----------------");
            System.out.println("1. Regiones");
            System.out.println("2. Comunas");
            System.out.println("3. Estaciones de una comuna");
            System.out.println("4. Sensores de una estación");
            System.out.println("5. Mediciones de un sensor");
            System.out.println("6. Volver");
            System.out.print("Opción: ");
            opcion = leerOpcion(1, 6);
            switch (opcion) {
                case 1:
                    listarRegiones();
                    break;
                case 2:
                    listarComunas();
                    break;
                case 3:
                    listarEstaciones();
                    break;
                case 4:
                    listarSensores();
                    break;
                case 5:
                    listarMediciones();
                    break;
                case 6:
                    break;
            }
        } while (opcion != 6);
    }

    private void listarRegiones() {
        String[][] datos = instituto.listaRegiones();
        System.out.println();
        System.out.println("REGIONES");
        System.out.println("--------");
        if (datos.length == 0) {
            System.out.println("No hay regiones registradas.");
            return;
        }
        System.out.printf("%-10s %-25s %-10s %-12s%n", "CÓDIGO", "NOMBRE", "COMUNAS", "ESTACIONES");
        for (String[] fila : datos) {
            System.out.printf("%-10s %-25s %-10s %-12s%n", fila[0], fila[1], fila[2], fila[3]);
        }
    }

    private void listarComunas() {
        String[][] datos = instituto.listaComunas();
        System.out.println();
        System.out.println("COMUNAS");
        System.out.println("-------");
        if (datos.length == 0) {
            System.out.println("No hay comunas registradas.");
            return;
        }
        System.out.printf("%-10s %-20s %-20s %-12s %-10s%n", "CÓDIGO", "NOMBRE", "REGIÓN", "ESTACIONES", "ACTIVAS");
        for (String[] fila : datos) {
            System.out.printf("%-10s %-20s %-20s %-12s %-10s%n", fila[0], fila[1], fila[2], fila[3], fila[4]);
        }
    }

    private void listarEstaciones() {
        System.out.print("Código de región: ");
        int codigoRegion = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Código de comuna: ");
        int codigoComuna = Integer.parseInt(sc.nextLine().trim());
        String[][] datos = instituto.listaEstaciones(codigoRegion, codigoComuna);
        System.out.println();
        System.out.println("ESTACIONES DE LA COMUNA " + codigoComuna);
        System.out.println("-------------------------------");
        if (datos.length == 0) {
            System.out.println("No hay estaciones registradas para esa comuna.");
            return;
        }
        System.out.printf("%-15s %-20s %-25s %-10s %-10s%n", "CÓDIGO", "NOMBRE", "UBICACIÓN", "ESTADO", "SENSORES");
        for (String[] fila : datos) {
            System.out.printf("%-15s %-20s %-25s %-10s %-10s%n", fila[0], fila[1], fila[2], fila[3], fila[4]);
        }
    }

    private void listarSensores() {
        System.out.print("Código de estación: ");
        String codigoEstacion = sc.nextLine().trim();
        String[][] datos = instituto.listaSensores(codigoEstacion);
        System.out.println();
        System.out.println("SENSORES DE " + codigoEstacion);
        System.out.println("-------------------------");
        if (datos.length == 0) {
            System.out.println("No hay sensores registrados para esa estación.");
            return;
        }
        System.out.printf("%-10s %-14s %-12s %-10s %-8s %-10s %-22s%n",
                "CÓDIGO", "TIPO", "MARCA", "MODELO", "UNIDAD", "ESTADO", "ÚLTIMA MEDICIÓN");
        for (String[] fila : datos) {
            System.out.printf("%-10s %-14s %-12s %-10s %-8s %-10s %-22s%n",
                    fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6]);
        }
    }

    private void listarMediciones() {
        System.out.print("Código de estación: ");
        String codigoEstacion = sc.nextLine().trim();
        System.out.print("Código de sensor: ");
        String codigoSensor = sc.nextLine().trim();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.print("Fecha y hora inicio [dd/MM/yyyy HH:mm]: ");
        LocalDateTime inicio = LocalDateTime.parse(sc.nextLine().trim(), formato);
        System.out.print("Fecha y hora fin [dd/MM/yyyy HH:mm]: ");
        LocalDateTime fin = LocalDateTime.parse(sc.nextLine().trim(), formato);
        String[][] datos = instituto.listaMediciones(codigoEstacion, codigoSensor, inicio, fin);
        System.out.println();
        System.out.println("MEDICIONES DEL SENSOR " + codigoSensor);
        System.out.println("-------------------------------");
        System.out.println("Período: " + inicio.format(formato) + " a " + fin.format(formato));
        if (datos.length == 0) {
            System.out.println("No hay mediciones registradas para ese período.");
            return;
        }
        System.out.printf("%-12s %-8s %-10s %-8s%n", "FECHA", "HORA", "VALOR", "UNIDAD");
        for (String[] fila : datos) {
            System.out.printf("%-12s %-8s %-10s %-8s%n", fila[0], fila[1], fila[2], fila[3]);
        }
    }

    private int leerOpcion(int min, int max) {
        int opcion = Integer.parseInt(sc.nextLine().trim());
        while (opcion < min || opcion > max) {
            System.out.print("Opción inválida. Intente nuevamente: ");
            opcion = Integer.parseInt(sc.nextLine().trim());
        }
        return opcion;
    }

    private TipoSensor obtenerTipoSensor(int opcion) {
        switch (opcion) {
            case 1:
                return TipoSensor.TEMPERATURA;
            case 2:
                return TipoSensor.HUMEDAD;
            case 3:
                return TipoSensor.PRESION;
            case 4:
                return TipoSensor.VIENTO;
            case 5:
                return TipoSensor.PRECIPITACION;
            default:
                return null;
        }
    }
}