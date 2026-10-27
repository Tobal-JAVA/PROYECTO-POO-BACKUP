package modelo; /**
 * Autor Noelia Andrea Montecinos Pinto
 */

import controlador.MeteorologiaException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Optional;

public abstract class Sensor {
    private String codigo;
    private String marca;
    private String modelo;
    private Estado estado;
    private List<Medicion> mediciones;
    private EstacionMeteorologica estacion;

    protected Sensor(String codigo, String marca, String modelo, EstacionMeteorologica estacion ){
        this.codigo=codigo;
        this.marca=marca;
        this.modelo=modelo;
        this.estacion= estacion;
        this.estado= Estado.ACTIVO;
        this.mediciones= new ArrayList<>();
    }

    public String getCodigo(){
        return codigo;
    }

    public String getMarca(){

        return marca;
    }

    public String getModelo(){
        return modelo;
    }

    public Estado getEstado(){

        return estado;
    }

    public void setEstado(Estado estado){

        this.estado=estado;
    }

    public void addMedicion(LocalDateTime fechaHora, float valor) throws MeteorologiaException{
        if(this.estado != Estado.ACTIVO){
            throw new MeteorologiaException("El sensor " + this.codigo + " no está activo.");
        }

        if(!esValorAdmisible(valor)){
            throw new MeteorologiaException("El valor " + String.format(java.util.Locale.US, "%.1f", valor) + " " + getUnidad() + " no es admisible para un sensor de tipo " + getTipo() + ".");
        }

        for(Medicion m : mediciones){
            if(m.getFechaHora().equals(fechaHora)){
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                throw new MeteorologiaException("El sensor " + this.codigo + " ya registra una medición el " + fechaHora.format(dateFormatter) + " a las " + fechaHora.format(timeFormatter) + ".");
            }
        }
        Medicion medicionNueva = new Medicion(fechaHora, valor);
        mediciones.add(medicionNueva);
        mediciones.sort(Comparator.comparing(Medicion::getFechaHora));

    }

    public Medicion[] getMediciones(){
        return mediciones.toArray(new  Medicion[0]);
    }

    public Medicion [] getMedicionesBetween(LocalDateTime inicio, LocalDateTime fin ){
        List<Medicion> filtradas = new ArrayList<>();

        for(Medicion m: mediciones){
            LocalDateTime fechaHora = m.getFechaHora();
            if ((fechaHora.isEqual(inicio) || fechaHora.isAfter(inicio)) && (fechaHora.isEqual(fin) || fechaHora.isBefore(fin))){
                filtradas.add(m);
            }
        }
        return filtradas.toArray(new Medicion[0]);
    }

    public Optional<ResumenMediciones> getResumenBetween(LocalDateTime inicio, LocalDateTime fin) {
        Medicion[] medEnRango = getMedicionesBetween(inicio, fin);
        if (medEnRango.length == 0) {
            return Optional.empty();
        }
        return Optional.of(new ResumenMediciones(medEnRango));
    }

    public abstract TipoSensor getTipo();
    public abstract String getUnidad();
    public abstract boolean esValorAdmisible(float valor);

}
