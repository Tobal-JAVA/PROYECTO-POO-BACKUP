import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Estacion meteorologica que administra sensores y sus mediciones.
 * Autores: [Completar nombres del equipo]
 */
public class EstacionMeteorologica {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final String codigo;
    private final String nombre;
    private final float longitud;
    private final float latitud;
    private final float altitud;
    private Estado estado;
    private final Comuna comuna;
    private final List<Sensor> sensores;

    public EstacionMeteorologica(String codigo, String nombre, float longitud,
                                 float latitud, float altitud, Comuna comuna) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.longitud = longitud;
        this.latitud = latitud;
        this.altitud = altitud;
        this.comuna = comuna;
        this.estado = Estado.ACTIVO;
        this.sensores = new ArrayList<>();
    }

    public boolean instalaSensor(String codigo, String marca, String modelo, TipoSensor tipo) {
        if (estado != Estado.ACTIVO || buscarSensor(codigo) != null
                || existeSensorActivoDelTipo(tipo)) {
            return false;
        }
        Sensor sensor = crearSensor(codigo, marca, modelo, tipo);
        return sensores.add(sensor);
    }

    public boolean registraMedicion(LocalDateTime fechaHora, float valor,
                                    String codigoSensor) {
        if (estado != Estado.ACTIVO) {
            return false;
        }
        Sensor sensor = buscarSensor(codigoSensor);
        return sensor != null && sensor.addMedicion(fechaHora, valor);
    }

    @Override
    public String toString() {
        String ubicacion = "(" + latitud + ", " + longitud + ", " + altitud + " m)";
        long sensoresOperativos = sensores.stream()
                .filter(s -> s.getEstado() == Estado.ACTIVO)
                .count();
        return codigo + "; " + nombre + "; " + ubicacion + "; " + estado
                + "; " + sensoresOperativos;
    }

    public String[][] getResumenSensores() {
        String[][] resumen = new String[sensores.size()][7];
        for (int i = 0; i < sensores.size(); i++) {
            Sensor sensor = sensores.get(i);
            Medicion ultima = sensor.getLastMedicion();
            resumen[i][0] = sensor.getCodigo();
            resumen[i][1] = tipoConcreto(sensor);
            resumen[i][2] = sensor.getMarca();
            resumen[i][3] = sensor.getModelo();
            resumen[i][4] = sensor.getUnidad();
            resumen[i][5] = sensor.getEstado().name();
            resumen[i][6] = ultima == null ? "Sin mediciones"
                    : ultima.toString() + " " + sensor.getUnidad();
        }
        return resumen;
    }

    public String[][] getMedicionesSensorBetween(String codigoSensor,
                                                  LocalDateTime inicio,
                                                  LocalDateTime fin) {
        Sensor sensor = buscarSensor(codigoSensor);
        if (sensor == null || inicio.isAfter(fin)) {
            return new String[0][4];
        }
        Medicion[] encontradas = sensor.getMedicionesBetween(inicio, fin);
        String[][] resultado = new String[encontradas.length][4];
        for (int i = 0; i < encontradas.length; i++) {
            resultado[i][0] = encontradas[i].getFechaHora().format(FECHA);
            resultado[i][1] = encontradas[i].getFechaHora().format(HORA);
            resultado[i][2] = String.format(Locale.US, "%.1f", encontradas[i].getValor());
            resultado[i][3] = sensor.getUnidad();
        }
        return resultado;
    }

    private Sensor buscarSensor(String codigoSensor) {
        return sensores.stream()
                .filter(s -> s.getCodigo().equalsIgnoreCase(codigoSensor))
                .findFirst()
                .orElse(null);
    }

    private boolean existeSensorActivoDelTipo(TipoSensor tipo) {
        return sensores.stream().anyMatch(s ->
                s.getEstado() == Estado.ACTIVO && tipoConcreto(s).equals(tipo.name()));
    }

    private Sensor crearSensor(String codigo, String marca, String modelo, TipoSensor tipo) {
        switch (tipo) {
            case HUMEDAD:
                return new SensorHumedad(codigo, marca, modelo, this);
            case TEMPERATURA:
                return new SensorTemperatura(codigo, marca, modelo, this);
            case PRESION:
                return new SensorPresion(codigo, marca, modelo, this);
            case VIENTO:
                return new SensorViento(codigo, marca, modelo, this);
            case PRECIPITACION:
                return new SensorPrecipitacion(codigo, marca, modelo, this);
            default:
                throw new IllegalArgumentException("Tipo de sensor no soportado");
        }
    }

    private String tipoConcreto(Sensor sensor) {
        return sensor.getClass().getSimpleName().replace("Sensor", "").toUpperCase();
    }
}
