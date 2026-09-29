/**
 * Autor Noelia Andrea Montecinos Pinto
 */
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public boolean addMedicion(LocalDateTime fechaHora, float valor){
        if(this.estado != Estado.ACTIVO){
            return false;
        }

        if(!esValorAdmisible(valor)){
            return false;
        }

        for(Medicion m : mediciones){
            if(m.getFechaHora().equals(fechaHora)){
                return false;
            }
        }
        Medicion medicionNueva = new Medicion(fechaHora, valor);
        return mediciones.add(medicionNueva);
    }
    public Medicion getLastMedicion(){
        if (mediciones.isEmpty()){
            return null;
        }
        return mediciones.get(mediciones.size()-1);
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

    public abstract String getUnidad();
    public abstract boolean esValorAdmisible(float valor);

}
