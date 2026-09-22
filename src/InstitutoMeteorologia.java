import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Fachada que comunica la interfaz con los objetos del dominio.
 * Autores: [Completar nombres del equipo]
 */
public class InstitutoMeteorologia {
    private final List<Region> regiones = new ArrayList<>();
    private final List<EstacionMeteorologica> estaciones = new ArrayList<>();

    public boolean creaRegion(int codigo, String nombre) {
        boolean existe = regiones.stream().anyMatch(r ->
                r.getCodigo() == codigo || r.getNombre().equalsIgnoreCase(nombre));
        return !existe && regiones.add(new Region(codigo, nombre));
    }

    public boolean creaComuna(int codigo, String nombre, int codigoRegion) {
        Region region = buscarRegion(codigoRegion);
        return region != null && region.addComuna(codigo, nombre);
    }

    public boolean creaEstacion(String codigo, String nombre, float longitud,
                                float latitud, float altitud, int codRegion,
                                int codComuna) {
        Region region = buscarRegion(codRegion);
        if (region == null || buscarEstacion(codigo) != null) {
            return false;
        }
        Comuna comuna = region.findComunaById(codComuna);
        if (comuna == null) {
            return false;
        }
        EstacionMeteorologica estacion = new EstacionMeteorologica(
                codigo, nombre, longitud, latitud, altitud, comuna);
        comuna.addEstacion(estacion);
        return estaciones.add(estacion);
    }

    public boolean instalaSensor(String codigo, String marca, String modelo,
                                 TipoSensor tipo, String codEstacion) {
        EstacionMeteorologica estacion = buscarEstacion(codEstacion);
        return estacion != null && estacion.instalaSensor(codigo, marca, modelo, tipo);
    }

    public boolean registraMedicion(LocalDateTime fechaHora, float valor,
                                    String codEstacion, String codSensor) {
        EstacionMeteorologica estacion = buscarEstacion(codEstacion);
        return estacion != null
                && estacion.registraMedicion(fechaHora, valor, codSensor);
    }

    public String[][] listaRegiones() {
        String[][] datos = new String[regiones.size()][4];
        for (int i = 0; i < regiones.size(); i++) {
            Region r = regiones.get(i);
            datos[i] = new String[]{String.valueOf(r.getCodigo()), r.getNombre(),
                    String.valueOf(r.getComunas().length),
                    String.valueOf(r.getCantidadEstaciones())};
        }
        return datos;
    }

    public String[][] listaComunas() {
        List<String[]> datos = new ArrayList<>();
        for (Region region : regiones) {
            for (Comuna comuna : region.getComunas()) {
                datos.add(new String[]{String.valueOf(comuna.getCodigo()),
                        comuna.getNombre(), region.getNombre(),
                        String.valueOf(comuna.getCantidadEstaciones()),
                        String.valueOf(comuna.getCantidadEstacionesActivas())});
            }
        }
        return datos.toArray(new String[0][]);
    }

    public String[][] listaEstaciones(int codigoRegion, int codigoComuna) {
        Region region = buscarRegion(codigoRegion);
        if (region == null) {
            return new String[0][5];
        }
        Comuna comuna = region.findComunaById(codigoComuna);
        if (comuna == null) {
            return new String[0][5];
        }
        List<String[]> datos = new ArrayList<>();
        for (EstacionMeteorologica estacion : estaciones) {
            String[] base = estacion.toString().split(";", -1);
            if (comuna.findEstacionById(base[0].trim()) == estacion) {
                datos.add(new String[]{base[0].trim(), base[1].trim(), base[2].trim(),
                        base[3].trim(), base[4].trim()});
            }
        }
        return datos.toArray(new String[0][]);
    }

    public String[][] listaSensores(String codigoEstacion) {
        EstacionMeteorologica estacion = buscarEstacion(codigoEstacion);
        return estacion == null ? new String[0][7] : estacion.getResumenSensores();
    }

    public String[][] listaMediciones(String codEstacion, String codSensor,
                                      LocalDateTime inicio, LocalDateTime fin) {
        EstacionMeteorologica estacion = buscarEstacion(codEstacion);
        return estacion == null ? new String[0][4]
                : estacion.getMedicionesSensorBetween(codSensor, inicio, fin);
    }

    private Region buscarRegion(int codigo) {
        return regiones.stream().filter(r -> r.getCodigo() == codigo)
                .findFirst().orElse(null);
    }

    private EstacionMeteorologica buscarEstacion(String codigo) {
        return estaciones.stream()
                .filter(e -> e.toString().split(";", -1)[0].trim()
                        .equalsIgnoreCase(codigo))
                .findFirst().orElse(null);
    }
}
