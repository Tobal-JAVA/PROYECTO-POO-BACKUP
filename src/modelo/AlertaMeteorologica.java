package modelo;

import java.time.LocalDateTime;

public class AlertaMeteorologica {
    private LocalDateTime fechaHora;
    private float valor;
    private NivelAlerta nivel;
    private String fenomeno;
    private Sensor sensor;

    public AlertaMeteorologica(LocalDateTime fechaHora, float valor, NivelAlerta nivel, String fenomeno, Sensor sensor){
        this.fechaHora=fechaHora;
        this.valor=valor;
        this.nivel=nivel;
        this.fenomeno=fenomeno;
        this.sensor=sensor;
    }
    public LocalDateTime getFechaHora(){
        return fechaHora;
    }

    public float getValor(){
        return valor;
    }
    public NivelAlerta getNivel(){
        return nivel;
    }

    public String getFenomeno(){
        return fenomeno;
    }

    public Sensor getSensor(){
        return sensor;
    }
}
