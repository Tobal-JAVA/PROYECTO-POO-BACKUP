import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Superclase abstracta para el comportamiento comun de los sensores.
 * Autores: [Completar nombres del equipo]
 */
public abstract class Sensor {
    private final String codigo;
    private final String marca;
    private final String modelo;
    private Estado estado;
    private final EstacionMeteorologica estacion;
    private final List<Medicion> mediciones;

    protected Sensor(String codigo, String marca, String modelo,
                     EstacionMeteorologica estacion) {
        this.codigo = codigo;
        this.marca = marca;
        this.modelo = modelo;
        this.estacion = estacion;
        this.estado = Estado.ACTIVO;
        this.mediciones = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public boolean addMedicion(LocalDateTime fechaHora, float valor) {
        if (estado != Estado.ACTIVO || !esValorAdmisible(valor)) {
            return false;
        }
        Medicion nueva = new Medicion(fechaHora, valor);
        if (mediciones.contains(nueva)) {
            return false;
        }
        return mediciones.add(nueva);
    }

    public Medicion getLastMedicion() {
        return mediciones.stream()
                .max(Comparator.comparing(Medicion::getFechaHora))
                .orElse(null);
    }

    public Medicion[] getMedicionesBetween(LocalDateTime inicio, LocalDateTime fin) {
        return mediciones.stream()
                .filter(m -> !m.getFechaHora().isBefore(inicio)
                        && !m.getFechaHora().isAfter(fin))
                .sorted(Comparator.comparing(Medicion::getFechaHora))
                .toArray(Medicion[]::new);
    }

    public abstract String getUnidad();

    public abstract boolean esValorAdmisible(float valor);
}
