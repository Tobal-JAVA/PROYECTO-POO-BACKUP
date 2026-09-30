import java.util.ArrayList;
/**
 * Autor Beatriz Aguilera
 */
public class Comuna {
    private int codigo;
    private String nombre;
    private Region region;
    private ArrayList<EstacionMeteorologica> estaciones;

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
    public Region getRegion() {
        return region;
    }
    public void addEstacion(EstacionMeteorologica estacion) {
        if (estacion != null) {
            estaciones.add(estacion);
        }
    }

    public EstacionMeteorologica findEstacionById(String codigo) {
        for (EstacionMeteorologica e : estaciones) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        return null;
    }

    public int getCantidadEstaciones() {
        return estaciones.size();
    }

    public int getCantidadEstacionesActivas() {
        int activas = 0;
        for (EstacionMeteorologica e : estaciones) {
            if (e.getEstado() == Estado.ACTIVO) {
                activas++;
            }
        }
        return activas;
    }
}
