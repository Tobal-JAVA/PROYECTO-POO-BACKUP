package controlador;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Persistenciatexto {

    public static final String SEPARADOR = ";";

    private static PersistenciaTexto instancia;

    private PersistenciaTexto() {
    }

    public static PersistenciaTexto getInstance() {
        if (instancia == null) {
            instancia = new PersistenciaTexto();
        }
        return instancia;
    }


    public static List<String[]> leeRegistros(String nombreArchivo) throws MeteorologiaException {
        Path ruta = rutaArchivo(nombreArchivo);
        if (!Files.exists(ruta)) {
            throw new MeteorologiaException("No se encontró el archivo " + nombreArchivo + ".");
        }
        List<String> lineas;
        try {
            lineas = new ArrayList<>(Files.readAllLines(ruta, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new MeteorologiaException("No fue posible leer el archivo " + nombreArchivo + ".");
        }
        if (!lineas.isEmpty() && lineas.get(0).startsWith("\uFEFF")) {
            lineas.set(0, lineas.get(0).substring(1));
        }
        while (!lineas.isEmpty() && lineas.get(lineas.size() - 1).trim().isEmpty()) {
            lineas.remove(lineas.size() - 1);
        }
        List<String[]> registros = new ArrayList<>();
        for (String linea : lineas) {
            registros.add(separaCampos(linea));
        }
        return registros;
    }


    public static void escribeRegistros(String nombreArchivo, List<String> lineas) throws MeteorologiaException {
        Path ruta = rutaArchivo(nombreArchivo);
        try {
            Files.createDirectories(ruta.getParent());
            Files.write(ruta, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new MeteorologiaException("No fue posible escribir el archivo " + nombreArchivo + ".");
        }
    }

    private static Path rutaArchivo(String nombreArchivo) {
        return Paths.get("datos", nombreArchivo);
    }

    private static String[] separaCampos(String linea) {
        String[] campos = linea.split(Pattern.quote(SEPARADOR), -1);
        for (int i = 0; i < campos.length; i++) {
            campos[i] = campos[i].trim();
        }
        return campos;
    }
}
