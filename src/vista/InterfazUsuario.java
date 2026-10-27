package vista;
/**
 *  Autor Beatriz Aguilera
 */
import controlador.InstitutoMeteorologia;
import controlador.MeteorologiaException;
import modelo.Estado;
import modelo.TipoSensor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InterfazUsuario {

    private static InterfazUsuario instancia;
    private Scanner sc;
    private InstitutoMeteorologia controlador;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private InterfazUsuario() {
        this.sc = new Scanner(System.in);
        this.controlador = InstitutoMeteorologia.getInstance();
    }

    public static InterfazUsuario getInstance() {
        if (instancia == null) {
            instancia = new InterfazUsuario();
        }
        return instancia;
    }

    public void menuPrincipal() {
        int opcion = 0;
        while (true){
            System.out.println("\nSISTEMA DE INFORMACIÓN METEOROLÓGICA");
            System.out.println("------------------------------------");
            System.out.println("1. Crear región");
            System.out.println("2. Crear comuna");
            System.out.println("3. Crear estación meteorológica");
            System.out.println("4. Instalar sensor");
            System.out.println("5. Registrar medición");
            System.out.println("6. Cambiar estado de sensor");
            System.out.println("7. Generar listados");
            System.out.println("8. Cargar datos");
            System.out.println("9. Guardar datos");
            System.out.println("10. Salir");

            opcion = leerEnteroEnRango("Opción: ", 1, 10);

            switch (opcion) {
                case 1: crearRegion(); break;
                case 2: crearComuna(); break;
                case 3: crearEstacionMeteorologica(); break;
                case 4: instalarSensor(); break;
                case 5: registrarMedicion(); break;
                case 6: cambiarEstadoSensor(); break;
                case 7: menuListados(); break;
                case 8: cargarDatos(); break;
                case 9: guardarDatos(); break;
                case 10: salir(); break;
            }
        }
    }

    private void crearRegion() {
        System.out.println("\nCREAR REGIÓN");
        int cod = leerEntero("Código: ");
        String nombre = leerTextoNoVacio("Nombre: ");
        try {
            controlador.creaRegion(cod, nombre);
            System.out.println("> Región creada exitosamente.");
        } catch (MeteorologiaException e) {
            System.out.println("> Error: " + e.getMessage());
        }
    }

    private void crearComuna() {
        System.out.println("\nCREAR COMUNA");
        int codRegion = leerEntero("Código de región: ");
        int codComuna = leerEntero("Código de comuna: ");
        String nombre = leerTextoNoVacio("Nombre: ");
        try {
            controlador.creaComuna(codComuna, nombre, codRegion);
            System.out.println("> modelo.Comuna creada exitosamente.");
        } catch (MeteorologiaException e) {
            System.out.println("> Error: " + e.getMessage());
        }
    }

    private void crearEstacionMeteorologica() {
        System.out.println("\nCREAR ESTACIÓN METEOROLÓGICA");
        String cod = leerTextoNoVacio("Código: ");
        String nombre = leerTextoNoVacio("Nombre: ");
        float lon = leerFloatEnRango("Longitud: ", -180, 180);
        float lat = leerFloatEnRango("Latitud: ", -90, 90);
        float alt = leerFloat("Altitud: ");
        int codRegion = leerEntero("Código de región: ");
        int codComuna = leerEntero("Código de comuna: ");
        try {
            controlador.creaEstacion(cod, nombre, lon, lat, alt, codRegion, codComuna);
            System.out.println("> Estación creada exitosamente.");
        } catch (MeteorologiaException e) {
            System.out.println("> Error: " + e.getMessage());
        }
    }

    private void instalarSensor() {
        System.out.println("\nINSTALAR SENSOR");
        String codEstacion = leerTextoNoVacio("Código de estación: ");
        TipoSensor[] tipos = { TipoSensor.TEMPERATURA, TipoSensor.HUMEDAD, TipoSensor.PRESION,
                TipoSensor.VIENTO, TipoSensor.PRECIPITACION };

        int opcTipo = leerEnteroEnRango("Tipo [1 Temp.  2 Hum.  3 Presión  4 Viento  5 Precip.]: ", 1, 5);
        TipoSensor tipo = tipos[opcTipo - 1];
        String codSensor = leerTextoNoVacio("Código de sensor: ");
        String marca = leerTextoNoVacio("Marca: ");
        String modelo = leerTextoNoVacio("Modelo: ");
        try {
            controlador.instalaSensor(codSensor, marca, modelo, tipo, codEstacion);
            System.out.println("> modelo.Sensor instalado exitosamente.");
        } catch (MeteorologiaException e) {
            System.out.println("> Error: " + e.getMessage());
        }
    }

    private void registrarMedicion() {
        System.out.println("\nREGISTRAR MEDICIÓN");
        String codEstacion = leerTextoNoVacio("Código de estación: ");
        String codSensor = leerTextoNoVacio("Código de sensor: ");
        LocalDateTime fechaHora = leerFechaHora("Fecha y hora [dd/MM/yyyy HH:mm]: ");
        float valor = leerFloat("Valor: ");
        try {
            controlador.registraMedicion(fechaHora, valor, codEstacion, codSensor);
            System.out.println("> Medición registrada exitosamente.");
        } catch (MeteorologiaException e) {
            System.out.println("> Error: " + e.getMessage());
        }
    }

    private void cambiarEstadoSensor() {
        System.out.println("\nCAMBIAR ESTADO DE SENSOR");
        String codEstacion = leerTextoNoVacio("Código de estación: ");
        String codSensor = leerTextoNoVacio("Código de sensor: ");
        int opcEstado = leerEnteroEnRango("Nuevo estado [1 ACTIVO 2 INACTIVO]: ", 1, 2);
        Estado estado = (opcEstado == 1) ? Estado.ACTIVO : Estado.INACTIVO;
        try {
            controlador.cambiaEstadoSensor(codEstacion, codSensor, estado);
            System.out.println("> modelo.Estado del sensor " + codSensor + " actualizado a " + estado + ".");
        } catch (MeteorologiaException e) {
            System.out.println("> Error: " + e.getMessage());
        }
    }

    private void cargarDatos() {
        System.out.println("\nCARGAR DATOS");
        try {
            controlador.cargaDatos();
            System.out.println("> Datos cargados correctamente desde la carpeta datos/.");
        } catch (MeteorologiaException e) {
            System.out.println("> Error: " + e.getMessage());
        }
    }

    private void guardarDatos() {
        System.out.println("\nGUARDAR DATOS");
        try {
            controlador.guardaDatos();
            System.out.println("> Datos guardados correctamente en la carpeta datos/.");
        } catch (MeteorologiaException e) {
            System.out.println("> Error: " + e.getMessage());
        }
    }

    private void salir() {
        String resp = leerSiNo("¿Desea guardar los datos antes de salir? [S/N]: ");
        if (resp.equals("S")) {
            try {
                controlador.guardaDatos();
                System.out.println("> Datos guardados correctamente en la carpeta datos/.");
            } catch (MeteorologiaException e) {
                System.out.println("> Error: " + e.getMessage());
                return;
            }
        }
        System.out.println("> Fin del programa.");
        System.exit(0);
    }

    private void menuListados() {
        int opc = 0;
        do {
            System.out.println("\nGENERAR LISTADOS");
            System.out.println("----------------");
            System.out.println("1. Regiones");
            System.out.println("2. Comunas");
            System.out.println("3. Estaciones de una comuna");
            System.out.println("4. Sensores de una estación");
            System.out.println("5. Mediciones de un sensor");
            System.out.println("6. Resumen estadístico de un sensor");
            System.out.println("7. Alertas de una estación");
            System.out.println("8. Volver al menú principal");

            opc = leerEnteroEnRango("Opción: ", 1, 8);

            switch (opc) {
                case 1: listarRegiones(); break;
                case 2: listarComunas(); break;
                case 3: listarEstaciones(); break;
                case 4: listarSensores(); break;
                case 5: listarMediciones(); break;
                case 6: listarResumenMediciones(); break;
                case 7: listarAlertas(); break;
            }
        } while (opc != 8);
    }

    private void listarRegiones() {
        String[][] datos = controlador.listaRegiones();
        if (datos.length == 0) {
            System.out.println("> No hay regiones registradas.");
            return;
        }
        System.out.println("\nREGIONES");
        System.out.printf("%-10s %-30s%n", "CÓDIGO", "NOMBRE");
        for (String[] fila : datos) {
            System.out.printf("%-10s %-30s%n", fila[0], fila[1]);
        }
    }

    private void listarComunas() {
        String[][] datos = controlador.listaComunas();
        if (datos.length == 0) {
            System.out.println("> No hay comunas registradas.");
            return;
        }
        System.out.println("\nCOMUNAS");
        System.out.printf("%-10s %-30s %-10s%n", "CÓDIGO", "NOMBRE", "REGIÓN");
        for (String[] fila : datos) {
            System.out.printf("%-10s %-30s %-10s%n", fila[0], fila[1], fila[2]);
        }
    }

    private void listarEstaciones() {
        int codRegion = leerEntero("Código de región: ");
        int codComuna = leerEntero("Código de comuna: ");
        try {
            String[][] datos = controlador.listaEstaciones(codRegion, codComuna);
            if (datos.length == 0) {
                System.out.println("> No hay estaciones registradas en la comuna.");
                return;
            }
            System.out.println("\nESTACIONES");
            System.out.printf("%-15s %-25s %-10s %-10s %-10s%n", "CÓDIGO", "NOMBRE", "LON", "LAT", "ALT");
            for (String[] f : datos) {
                System.out.printf("%-15s %-25s %-10s %-10s %-10s%n", f[0], f[1], f[2], f[3], f[4]);
            }
        } catch (MeteorologiaException e) {
            System.out.println("> Error: " + e.getMessage());
        }
    }

    private void listarSensores() {
        String codEstacion = leerTextoNoVacio("Código de estación: ");
        try {
            String[][] datos = controlador.listaSensores(codEstacion);
            if (datos.length == 0) {
                System.out.println("> La estación " + codEstacion + " no tiene sensores instalados.");
                return;
            }
            System.out.println("\nSENSORES DE LA ESTACIÓN " + codEstacion);
            System.out.printf("%-12s %-15s %-15s %-15s %-10s%n", "CÓDIGO", "TIPO", "MARCA", "MODELO", "ESTADO");
            for (String[] f : datos) {
                System.out.printf("%-12s %-15s %-15s %-15s %-10s%n", f[0], f[1], f[2], f[3], f[4]);
            }
        } catch (MeteorologiaException e) {
            System.out.println("> Error: " + e.getMessage());
        }
    }

    private void listarMediciones() {
        String codEstacion = leerTextoNoVacio("Código de estación: ");
        String codSensor = leerTextoNoVacio("Código de sensor: ");
        LocalDateTime inicio = leerFechaHora("Inicio [dd/MM/yyyy HH:mm]: ");
        LocalDateTime fin = leerFechaHora("Fin    [dd/MM/yyyy HH:mm]: ");
        try {
            String[][] datos = controlador.listaMediciones(codEstacion, codSensor, inicio, fin);
            if (datos.length == 0) {
                System.out.println("> No se registran mediciones en el período indicado.");
                return;
            }
            System.out.println("\nMEDICIONES DEL SENSOR " + codSensor);
            System.out.printf("%-20s %-10s%n", "FECHA Y HORA", "VALOR");
            for (String[] f : datos) {
                System.out.printf("%-20s %-10s%n", f[0], f[1]);
            }
        } catch (MeteorologiaException e) {
            System.out.println("> Error: " + e.getMessage());
        }
    }

    private void listarResumenMediciones() {
        String codEstacion = leerTextoNoVacio("Código de estación: ");
        String codSensor = leerTextoNoVacio("Código de sensor: ");
        LocalDateTime inicio = leerFechaHora("Inicio [dd/MM/yyyy HH:mm]: ");
        LocalDateTime fin = leerFechaHora("Fin    [dd/MM/yyyy HH:mm]: ");
        try {

            String[] r = controlador.listaResumenMediciones(codEstacion, codSensor, inicio, fin);
            if (r.length == 0) {
                System.out.println("> El sensor " + codSensor + " no registra mediciones en el período indicado.");
                return;
            }
            System.out.println("\nRESUMEN ESTADÍSTICO DEL SENSOR " + codSensor + " (" + codEstacion + ")");
            System.out.println("Período: " + inicio.format(FORMATO_FECHA) + " a " + fin.format(FORMATO_FECHA));
            System.out.println("Tipo de sensor        : " + r[0]);
            System.out.println("Cantidad de mediciones: " + r[2]);
            System.out.println("Mínimo                : " + r[3] + " " + r[1] + " (" + r[4] + ")");
            System.out.println("Máximo                : " + r[5] + " " + r[1] + " (" + r[6] + ")");
            System.out.println("Promedio              : " + r[7] + " " + r[1]);
        } catch (MeteorologiaException e) {
            System.out.println("> Error: " + e.getMessage());
        }
    }

    private void listarAlertas() {
        String codEstacion = leerTextoNoVacio("Código de estación: ");
        LocalDateTime inicio = leerFechaHora("Inicio [dd/MM/yyyy HH:mm]: ");
        LocalDateTime fin = leerFechaHora("Fin    [dd/MM/yyyy HH:mm]: ");
        try {
            String[][] datos = controlador.listaAlertas(codEstacion, inicio, fin);
            if (datos.length == 0) {
                System.out.println("> La estación " + codEstacion + " no registra alertas en el período indicado.");
                return;
            }
            System.out.println("\nALERTAS DE LA ESTACIÓN " + codEstacion);
            System.out.println("Período: " + inicio.format(FORMATO_FECHA) + " a " + fin.format(FORMATO_FECHA));
            System.out.printf("%-12s %-8s %-10s %-25s %-12s %-10s%n", "FECHA", "HORA", "SENSOR", "FENÓMENO", "VALOR", "NIVEL");
            for (String[] f : datos) {

                String valConUnidad = f[4] + " " + f[5];
                System.out.printf("%-12s %-8s %-10s %-25s %-12s %-10s%n", f[0], f[1], f[2], f[3], valConUnidad, f[6]);
            }
        } catch (MeteorologiaException e) {
            System.out.println("> Error: " + e.getMessage());
        }
    }

    private String leerTextoNoVacio(String mensaje) {
        String texto;
        do {
            System.out.print(mensaje);
            texto = sc.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("> El campo no puede estar vacío.");
            }
        } while (texto.isEmpty());
        return texto;
    }

    private String leerSiNo(String mensaje) {
        String r;
        do {
            r = leerTextoNoVacio(mensaje) .toUpperCase();
            if (!r.equals("S") && !r.equals("N")) {
                System.out.println("> Responda S o N.");
            }
        } while (!r.equals("S") && !r.equals("N"));
        return r;
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String entrada = sc.nextLine().trim();
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("> Debe ingresar un número entero válido.");
            }
        }
    }
    private int leerEnteroEnRango(String mensaje, int min, int max) {
        int valor;
        do {
            valor = leerEntero(mensaje);
            if (valor < min || valor > max) {
                System.out.println("> Opción invalida. Ingrese un valor entre " + min + " y " + max + ".");
            }
        } while (valor < min || valor > max);
        return valor;
    }

    private float leerFloat(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String entrada = sc.nextLine().trim().replace(',', '.');
                return Float.parseFloat(entrada);
            } catch (NumberFormatException e) {
                System.out.println("> Debe ingresar un numero decimal valido.");
            }
        }
    }
    private float leerFloatEnRango(String mensaje, float min, float max) {
        float valor;
        do {
            valor = leerFloat(mensaje);
            if (valor < min || valor > max) {
                System.out.println("> Valor fuera de rango. Ingrese un valor entre " + min + " y " + max + ".");
            }
        } while (valor < min || valor > max);
        return valor;
    }

    private LocalDateTime leerFechaHora(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String entrada = sc.nextLine().trim();
                return LocalDateTime.parse(entrada, FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("> Formato invalido. Use el formato dd/MM/yyyy HH:mm (ej. 24/08/2026 15:30).");
            }
        }
    }
}