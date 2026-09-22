import java.util.ArrayList;
import java.util.List;

/**
 * Region administrativa que contiene comunas.
 * Autores: [Completar nombres del equipo]
 */
public class Region {
    private final int codigo;
    private final String nombre;
    private final List<Comuna> comunas;

    public Region(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.comunas = new ArrayList<>();
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean addComuna(int codigo, String nombre) {
        boolean existe = comunas.stream().anyMatch(c ->
                c.getCodigo() == codigo || c.getNombre().equalsIgnoreCase(nombre));
        if (existe) {
            return false;
        }
        return comunas.add(new Comuna(codigo, nombre, this));
    }

    public Comuna findComunaById(int codigo) {
        return comunas.stream()
                .filter(c -> c.getCodigo() == codigo)
                .findFirst()
                .orElse(null);
    }

    public Comuna[] getComunas() {
        return comunas.toArray(new Comuna[0]);
    }

    public int getCantidadEstaciones() {
        return comunas.stream().mapToInt(Comuna::getCantidadEstaciones).sum();
    }
}
