import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class EstacionMeteorologica {

    private String codigo;
    private String nombre;
    private float longitud;
    private float latitud;
    private float altitud;
    private Estado estado;
    private Comuna comuna;
    private ArrayList<Sensor> sensores;

    public EstacionMeteorologica(String codigo, String nombre,
                                 float longitud, float latitud, float altitud, Comuna comuna) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.longitud = longitud;
        this.latitud = latitud;
        this.altitud = altitud;
        this.estado = Estado.ACTIVO;
        this.comuna = comuna;
        this.sensores = new ArrayList<>();
    }

    String getCodigo() {
        return codigo;
    }

    Estado getEstado() {
        return estado;
    }

    public boolean instalaSensor(String codigo, String marca,
                                 String modelo, TipoSensor tipo) {

        if (this.estado != Estado.ACTIVO) {
            return false;
        }

        for (Sensor s : sensores) {

            if (s.getCodigo().equals(codigo)) {
                return false;
            }

            if (s.getEstado() == Estado.ACTIVO
                    && sensorCoincideConTipo(s, tipo)) {
                return false;
            }
        }

        Sensor nuevoSensor;

        switch (tipo) {

            case TEMPERATURA:
                nuevoSensor = new SensorTemperatura(
                        codigo, marca, modelo, this);
                break;

            case HUMEDAD:
                nuevoSensor = new SensorHumedad(
                        codigo, marca, modelo, this);
                break;

            case PRESION:
                nuevoSensor = new SensorPresion(
                        codigo, marca, modelo, this);
                break;

            case VIENTO:
                nuevoSensor = new SensorViento(
                        codigo, marca, modelo, this);
                break;

            case PRECIPITACION:
                nuevoSensor = new SensorPrecipitacion(
                        codigo, marca, modelo, this);
                break;

            default:
                return false;
        }

        sensores.add(nuevoSensor);
        return true;
    }

    public boolean registraMedicion(LocalDateTime fechaHora,
                                    float valor, String codigoSensor) {

        if (this.estado != Estado.ACTIVO) {
            return false;
        }

        Sensor s = findSensorByCodigo(codigoSensor);

        if (s == null) {
            return false;
        }

        return s.addMedicion(fechaHora, valor);
    }

    private int getCantidadSensoresOperativos() {

        int operativos = 0;

        for (Sensor s : sensores) {

            if (s.getEstado() == Estado.ACTIVO) {
                operativos++;
            }
        }

        return operativos;
    }

    @Override

    public String toString() {

        return String.format(Locale.US,
                "%s; %s; (%.4f; %.4f; %.0f m); %s; %d",
                codigo,
                nombre,
                latitud,
                longitud,
                altitud,
                estado,
                getCantidadSensoresOperativos());
    }

    public String[][] getResumenSensores() {

        if (sensores.isEmpty()) {
            return new String[0][0];
        }

        String[][] matriz = new String[sensores.size()][7];

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (int i = 0; i < sensores.size(); i++) {

            Sensor s = sensores.get(i);

            matriz[i][0] = s.getCodigo();
            matriz[i][1] = obtenerTipoConcreto(s);
            matriz[i][2] = s.getMarca();
            matriz[i][3] = s.getModelo();
            matriz[i][4] = s.getUnidad();
            matriz[i][5] = s.getEstado().toString();

            Medicion ultima = s.getLastMedicion();

            if (ultima != null) {

                matriz[i][6] = String.format(Locale.US,
                        "%s %.1f %s",
                        ultima.getFechaHora().format(formato),
                        ultima.getValor(),
                        s.getUnidad());

            } else {

                matriz[i][6] = "sin mediciones";
            }
        }

        return matriz;
    }

    public String[][] getMedicionesSensorBetween(
            String codigoSensor,
            LocalDateTime inicio,
            LocalDateTime fin) {

        Sensor s = findSensorByCodigo(codigoSensor);

        if (s == null) {
            return new String[0][0];
        }

        Medicion[] mediciones =
                s.getMedicionesBetween(inicio, fin);

        if (mediciones == null || mediciones.length == 0) {
            return new String[0][0];
        }

        String[][] matriz = new String[mediciones.length][4];

        DateTimeFormatter formatoFecha =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        DateTimeFormatter formatoHora =
                DateTimeFormatter.ofPattern("HH:mm");

        for (int i = 0; i < mediciones.length; i++) {

            Medicion m = mediciones[i];

            matriz[i][0] =
                    m.getFechaHora().format(formatoFecha);

            matriz[i][1] =
                    m.getFechaHora().format(formatoHora);

            matriz[i][2] =
                    String.format(Locale.US, "%.1f", m.getValor());

            matriz[i][3] =
                    s.getUnidad();
        }

        return matriz;
    }

    private Sensor findSensorByCodigo(String codigo) {

        for (Sensor s : sensores) {

            if (s.getCodigo().equals(codigo)) {
                return s;
            }
        }

        return null;
    }

    private boolean sensorCoincideConTipo(
            Sensor s, TipoSensor tipo) {

        switch (tipo) {

            case TEMPERATURA:
                return s instanceof SensorTemperatura;

            case HUMEDAD:
                return s instanceof SensorHumedad;

            case PRESION:
                return s instanceof SensorPresion;

            case VIENTO:
                return s instanceof SensorViento;

            case PRECIPITACION:
                return s instanceof SensorPrecipitacion;

            default:
                return false;
        }
    }

    private String obtenerTipoConcreto(Sensor s) {

        if (s instanceof SensorTemperatura) {
            return "TEMPERATURA";
        }

        if (s instanceof SensorHumedad) {
            return "HUMEDAD";
        }

        if (s instanceof SensorPresion) {
            return "PRESIÓN";
        }

        if (s instanceof SensorViento) {
            return "VIENTO";
        }

        if (s instanceof SensorPrecipitacion) {
            return "PRECIPITACIÓN";
        }

        return "DESCONOCIDO";
    }
}