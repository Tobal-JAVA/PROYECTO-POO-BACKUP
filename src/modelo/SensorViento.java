package modelo;

import java.util.Optional;

/**
 * Autor Noelia Andrea Montecinos Pinto
 */
public class SensorViento extends Sensor implements Alertable {

    public SensorViento(String codigo, String marca, String modelo, EstacionMeteorologica estacion) {
        super(codigo, marca, modelo, estacion);
    }

    public TipoSensor getTipo(){
        return TipoSensor.VIENTO;
    }

    @Override
    public String getUnidad() {

        return "km/h";
    }

    @Override
    public boolean esValorAdmisible(float valor) {
        return valor >= 0.0f && valor <= 250.0f;
    }

    @Override
    public Optional<NivelAlerta> evaluaAlerta(float valor) {
        if (valor >= 90.0f) return Optional.of(NivelAlerta.ALARMA);
        if (valor >= 70.0f) return Optional.of(NivelAlerta.ALERTA);
        if (valor >= 50.0f) return Optional.of(NivelAlerta.AVISO);
        return Optional.empty();
    }

    @Override
    public String getFenomeno(float valor) {
        if (valor >= 50.0f) {
            return "Viento intenso";
        }
        return "";
    }
}

