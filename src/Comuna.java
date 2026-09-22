import java.util.ArrayList;
import java.util.List;

/**
 * Comuna asociada a una region y a sus estaciones.
 * Autores: [Completar nombres del equipo]
 */
public class Comuna {
    private final int codigo;
    private final String nombre;
    private final Region region;
    private final List<EstacionMeteorologica> estaciones;

    public Comuna(int codigo, String nombre, Region region) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.region = region;
        this.estaciones = new ArrayList<>();
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void addEstacion(EstacionMeteorologica estacion) {
        estaciones.add(estacion);
    }

    public EstacionMeteorologica findEstacionById(String codigo) {
        return estaciones.stream()
                .filter(e -> obtenerCodigo(e).equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);
    }

    public Region getRegion() {
        return region;
    }

    public int getCantidadEstaciones() {
        return estaciones.size();
    }

    public int getCantidadEstacionesActivas() {
        return (int) estaciones.stream()
                .filter(e -> obtenerEstado(e) == Estado.ACTIVO)
                .count();
    }

    private String obtenerCodigo(EstacionMeteorologica estacion) {
        return estacion.toString().split(";", -1)[0].trim();
    }

    private Estado obtenerEstado(EstacionMeteorologica estacion) {
        String[] datos = estacion.toString().split(";", -1);
        return Estado.valueOf(datos[3].trim());
    }
}
