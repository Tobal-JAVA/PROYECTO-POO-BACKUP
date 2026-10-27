package modelo;

public class ResumenMediciones {
    private final int cantidad;
    private final float promedio;
    private final Medicion minimo;
    private final Medicion maximo;

    public  ResumenMediciones(Medicion[] mediciones){
        this.cantidad = mediciones.length;

        Medicion min = mediciones[0];
        Medicion max = mediciones[0];
        float suma = mediciones[0].getValor();

        for (int i = 1; i < mediciones.length; i++) {
            float valorActual = mediciones[i].getValor();
            suma += valorActual;

            if (valorActual < min.getValor()) {
                min = mediciones[i];
            }

            if (valorActual > max.getValor()) {
                max = mediciones[i];
            }
        }

        this.minimo = min;
        this.maximo = max;
        this.promedio = suma / this.cantidad;
    }

    public int getCantidad(){
        return cantidad;
    }

    public float getPromedio(){
        return promedio;
    }

    public Medicion getMinimo(){
        return minimo;
    }

    public Medicion getMaximo(){
        return maximo;
    }

}
