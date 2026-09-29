/**
 * Autor Cristobal Quezada
 */

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InstitutoMeteorologia {

    private List<Region> regiones = new ArrayList<>();
    private List<EstacionMeteorologica> estaciones = new ArrayList<>();

    public boolean creaRegion(int codigo, String nombre) {
        if (existeRegion(codigo, nombre)) {
            return false;
        }
        regiones.add(new Region(codigo, nombre));
        return true;
    }

    public boolean creaComuna(int codigo, String nombre, int codigoRegion) {
        Region region = buscarRegionPorId(codigoRegion);
        if (region == null) {
            return false;
        }
        return region.addComuna(codigo, nombre);
    }

    public boolean creaEstacion(String cod, String nombre, float lon, float lat, float alt, int codRegion, int codComuna) {
        Region region = buscarRegionPorId(codRegion);
        if (region == null) {
            return false;
        }
        Comuna comuna = region.findComunaById(codComuna);
        if (comuna == null) {
            return false;
        }
        if (existeEstacion(cod)) {
            return false;
        }

        EstacionMeteorologica estacion = new EstacionMeteorologica(cod, nombre, lon, lat, alt, comuna);
        estaciones.add(estacion);
        comuna.addEstacion(estacion);
        return true;
    }

    public boolean instalaSensor(String cod, String marca, String modelo, TipoSensor tipo, String codigoEstacion) {
        EstacionMeteorologica estacion = buscarEstacionPorId(codigoEstacion);
        if (estacion == null) {
            return false;
        }
        return estacion.instalaSensor(cod, marca, modelo, tipo);
    }

    public boolean registraMedicion(LocalDateTime fechaHora, float valor, String codEstacion, String codSensor) {
        EstacionMeteorologica estacion = buscarEstacionPorId(codEstacion);
        if (estacion == null) {
            return false;
        }
        return estacion.registraMedicion(fechaHora, valor, codSensor);
    }

    public String[][] listaRegiones() {
        if (regiones.isEmpty()) {
            return new String[0][0];
        }
        String[][] resultado = new String[regiones.size()][4];
        for (int i = 0; i < regiones.size(); i++) {
            Region r = regiones.get(i);
            resultado[i][0] = String.valueOf(r.getCodigo());
            resultado[i][1] = r.getNombre();
            resultado[i][2] = String.valueOf(r.getComunas().length);
            resultado[i][3] = String.valueOf(r.getCantidadEstaciones());
        }
        return resultado;
    }

    public String[][] listaComunas() {
        List<Comuna> todas = new ArrayList<>();
        for (Region r : regiones) {
            for (Comuna c : r.getComunas()) {
                todas.add(c);
            }
        }
        if (todas.isEmpty()) {
            return new String[0][0];
        }

        String[][] resultado = new String[todas.size()][5];
        for (int i = 0; i < todas.size(); i++) {
            Comuna c = todas.get(i);
            resultado[i][0] = String.valueOf(c.getCodigo());
            resultado[i][1] = c.getNombre();
            resultado[i][2] = c.getRegion().getNombre();
            resultado[i][3] = String.valueOf(c.getCantidadEstaciones());
            resultado[i][4] = String.valueOf(c.getCantidadEstacionesActivas());
        }
        return resultado;
    }

    public String[][] listaEstaciones(int codigoRegion, int codigoComuna) {
        Region region = buscarRegionPorId(codigoRegion);
        if (region == null) {
            return new String[0][0];
        }
        Comuna comuna = region.findComunaById(codigoComuna);
        if (comuna == null) {
            return new String[0][0];
        }

        List<EstacionMeteorologica> filtradas = new ArrayList<>();
        for (EstacionMeteorologica e : estaciones) {
            if (comuna.findEstacionById(obtenerCodigoEstacion(e)) != null) {
                filtradas.add(e);
            }
        }

        if (filtradas.isEmpty()) {
            return new String[0][0];
        }

        String[][] resultado = new String[filtradas.size()][5];
        for (int i = 0; i < filtradas.size(); i++) {
            resultado[i] = filtradas.get(i).toString().split("; ");
        }
        return resultado;
    }

    public String[][] listaSensores(String codigoEstacion) {
        EstacionMeteorologica estacion = buscarEstacionPorId(codigoEstacion);
        if (estacion == null) {
            return new String[0][0];
        }
        return estacion.getResumenSensores();
    }

    public String[][] listaMediciones(String codEstacion, String codSensor, LocalDateTime inicio, LocalDateTime fin) {
        EstacionMeteorologica estacion = buscarEstacionPorId(codEstacion);
        if (estacion == null) {
            return new String[0][0];
        }
        return estacion.getMedicionesSensorBetween(codSensor, inicio, fin);
    }

    private boolean existeRegion(int codigo, String nombre) {
        for (Region r : regiones) {
            if (r.getCodigo() == codigo || r.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    private Region buscarRegionPorId(int codigo) {
        for (Region r : regiones) {
            if (r.getCodigo() == codigo) {
                return r;
            }
        }
        return null;
    }

    private boolean existeEstacion(String codigo) {
        return buscarEstacionPorId(codigo) != null;
    }

    private EstacionMeteorologica buscarEstacionPorId(String codigo) {
        for (EstacionMeteorologica e : estaciones) {
            if (obtenerCodigoEstacion(e).equalsIgnoreCase(codigo)) {
                return e;
            }
        }
        return null;
    }

    private String obtenerCodigoEstacion(EstacionMeteorologica estacion) {
        return estacion.toString().split("; ")[0];
    }
}