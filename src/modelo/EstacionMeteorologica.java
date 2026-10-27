package modelo;

import controlador.MeteorologiaException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
/**
 * Autor Beatriz Aguilera / Modificado Noelia Montecinos
 */
public class EstacionMeteorologica {

    private String codigo;
    private String nombre;
    private float longitud;
    private float latitud;
    private float altitud;
    private Estado estado;
    private Comuna comuna;
    private ArrayList<Sensor> sensores;
    private ArrayList<AlertaMeteorologica> alertas;

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
        this.alertas = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void instalaSensor(String codigo, String marca,
                              String modelo, TipoSensor tipo) throws MeteorologiaException {

        if (this.estado != Estado.ACTIVO) {
            throw new MeteorologiaException("La estación " + this.codigo + " no está activa.");
        }

        for (Sensor s : sensores) {
            if (s.getCodigo().equals(codigo)) {
                throw new MeteorologiaException("Ya existe un sensor con el código " + codigo + " en la estación " + this.codigo + ".");
            }

            if (s.getEstado() == Estado.ACTIVO && sensorCoincideConTipo(s, tipo)) {
                throw new MeteorologiaException("La estación " + this.codigo + " ya tiene un sensor ACTIVO de tipo " + tipo + " (" + s.getCodigo() + ").");
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
                throw new MeteorologiaException("Tipo de sensor no válido.");
        }

        sensores.add(nuevoSensor);

    }

    public void cambiaEstadoSensor(String codigoSensor, Estado nuevoEstado) throws MeteorologiaException {
        Optional<Sensor> sensorOpt = findSensorByCodigo(codigoSensor);

        if (!sensorOpt.isPresent()) {
            throw new MeteorologiaException("No existe el sensor " + codigoSensor + " en la estación " + this.codigo + ".");
        }

        Sensor s = sensorOpt.get();

        if (s.getEstado() == nuevoEstado) {
            throw new MeteorologiaException("El sensor " + codigoSensor + " ya se encuentra en estado " + nuevoEstado + ".");
        }


        if (nuevoEstado == Estado.ACTIVO) {
            for (Sensor existente : sensores) {
                if (existente.getEstado() == Estado.ACTIVO && existente.getTipo() == s.getTipo() && !existente.getCodigo().equals(codigoSensor)) {
                    throw new MeteorologiaException("No es posible activar el sensor " + codigoSensor + ": la estación ya tiene activo el sensor " + existente.getCodigo() + " de tipo " + s.getTipo() + ".");
                }
            }
        }


        s.setEstado(nuevoEstado);
    }

    public void registraMedicion(LocalDateTime fechaHora,
                                 float valor, String codigoSensor) throws MeteorologiaException {

        if (this.estado != Estado.ACTIVO) {
            throw new MeteorologiaException("La estación " + this.codigo + " no está activa.");
        }

        Optional<Sensor> sensorOpt = findSensorByCodigo(codigoSensor);

        if (!sensorOpt.isPresent()) {
            throw new MeteorologiaException("No existe el sensor " + codigoSensor + " en la estación " + this.codigo + ".");
        }

        Sensor s = sensorOpt.get();

        s.addMedicion(fechaHora, valor);

        if (s instanceof Alertable) {
            Alertable alertableSensor = (Alertable) s;
            Optional<NivelAlerta> nivelOpt = alertableSensor.evaluaAlerta(valor);
            if (nivelOpt.isPresent()) {
                String fenomeno = alertableSensor.getFenomeno(valor);
                AlertaMeteorologica alerta = new AlertaMeteorologica(fechaHora, valor, nivelOpt.get(), fenomeno, s);
                this.alertas.add(alerta);
            }
        }
    }
    public AlertaMeteorologica[] getAlertasBetween(LocalDateTime inicio, LocalDateTime fin) {
        ArrayList<AlertaMeteorologica> filtradas = new ArrayList<>();
        for (AlertaMeteorologica a : alertas) {
            LocalDateTime fechaAlerta = a.getFechaHora();

            if (!fechaAlerta.isBefore(inicio) && !fechaAlerta.isAfter(fin)) {
                filtradas.add(a);
            }
        }
        return filtradas.toArray(new AlertaMeteorologica[0]);
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
                "%s; %s; (%.4f, %.4f, %.0f m); %s; %d",
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

            Medicion[] meds = s.getMediciones();
            Medicion ultima = (meds != null && meds.length > 0) ? meds[meds.length - 1] : null;

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

        Optional<Sensor> sensorOpt = findSensorByCodigo(codigoSensor);

        if (!sensorOpt.isPresent()) {
            return new String[0][0];
        }

        Sensor s = sensorOpt.get();

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

    private Optional<Sensor> findSensorByCodigo(String codigo) {
        for (Sensor s : sensores) {
            if (s.getCodigo().equals(codigo)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
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