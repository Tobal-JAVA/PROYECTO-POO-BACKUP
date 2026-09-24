import java.time.LocalDateTime;
import java.util.ArrayList;

public class InstitutoMeteorologia {

    private ArrayList<Region> listaRegiones;
    private ArrayList<EstacionMeteorologica> listaEstaciones;

    public InstitutoMeteorologia() {
        this.listaRegiones = new ArrayList<>();
        this.listaEstaciones = new ArrayList<>();
    }

    private Region buscarRegion(int codigoRegion) {
        for (Region regionActual : this.listaRegiones) {
            if (regionActual.getCodigo() == codigoRegion) {
                return regionActual;
            }
        }
        return null;
    }

    private EstacionMeteorologica buscarEstacion(String codigoEstacion) {
        for (EstacionMeteorologica estacionActual : this.listaEstaciones) {
            if (estacionActual.getCodigo().equalsIgnoreCase(codigoEstacion)) {
                return estacionActual;
            }
        }
        return null;
    }

    public boolean creaRegion(int codigo, String nombre) {
        for (Region regionActual : this.listaRegiones) {
            if (regionActual.getCodigo() == codigo) {
                return false;
            }
            if (regionActual.getNombre().equalsIgnoreCase(nombre)) {
                return false;
            }
        }

        Region nuevaRegion = new Region(codigo, nombre);
        return this.listaRegiones.add(nuevaRegion);
    }

    public boolean creaComuna(int codigo, String nombre, int codigoRegion) {
        Region regionEncontrada = buscarRegion(codigoRegion);

        if (regionEncontrada != null) {
            return regionEncontrada.addComuna(codigo, nombre);
        }

        return false;
    }

    public boolean creaEstacion(String codigo, String nombre, float longitud,
                                float latitud, float altitud, int codigoRegion,
                                int codigoComuna) {

        Region regionEncontrada = buscarRegion(codigoRegion);
        if (regionEncontrada == null) {
            return false;
        }

        Comuna comunaEncontrada = regionEncontrada.findComunaById(codigoComuna);
        if (comunaEncontrada == null) {
            return false;
        }

        EstacionMeteorologica estacionExistente = buscarEstacion(codigo);
        if (estacionExistente != null) {
            return false;
        }

        EstacionMeteorologica nuevaEstacion = new EstacionMeteorologica(
                codigo, nombre, longitud, latitud, altitud, comunaEncontrada);

        comunaEncontrada.addEstacion(nuevaEstacion);
        this.listaEstaciones.add(nuevaEstacion);
        return true;
    }

    public boolean instalaSensor(String codigo, String marca, String modelo,
                                 TipoSensor tipo, String codigoEstacion) {

        EstacionMeteorologica estacionEncontrada = buscarEstacion(codigoEstacion);
        if (estacionEncontrada != null) {
            return estacionEncontrada.instalaSensor(codigo, marca, modelo, tipo);
        }

        return false;
    }

    public boolean registraMedicion(LocalDateTime fechaHora, float valor,
                                    String codigoEstacion, String codigoSensor) {

        EstacionMeteorologica estacionEncontrada = buscarEstacion(codigoEstacion);
        if (estacionEncontrada != null) {
            return estacionEncontrada.registraMedicion(codigoSensor, fechaHora, valor);
        }

        return false;
    }

    public String[][] listaRegiones() {
        int tamañoRegiones = this.listaRegiones.size();
        if (tamañoRegiones == 0) {
            return new String[0][4];
        }

        String[][] matrizRegiones = new String[tamañoRegiones][4];

        for (int i = 0; i < tamañoRegiones; i++) {
            Region regionActual = this.listaRegiones.get(i);

            matrizRegiones[i][0] = String.valueOf(regionActual.getCodigo());
            matrizRegiones[i][1] = regionActual.getNombre();
            matrizRegiones[i][2] = String.valueOf(regionActual.getComunas().length);
            matrizRegiones[i][3] = String.valueOf(regionActual.getCantidadEstaciones());
        }

        return matrizRegiones;
    }

    public String[][] listaComunas() {
        ArrayList<String[]> listaTemporal = new ArrayList<>();

        for (Region regionActual : this.listaRegiones) {
            Comuna[] arregloComunas = regionActual.getComunas();

            for (Comuna comunaActual : arregloComunas) {
                String[] filaComuna = new String[5];
                filaComuna[0] = String.valueOf(comunaActual.getCodigo());
                filaComuna[1] = comunaActual.getNombre();
                filaComuna[2] = regionActual.getNombre();
                filaComuna[3] = String.valueOf(comunaActual.getCantidadEstaciones());
                filaComuna[4] = String.valueOf(comunaActual.getCantidadEstacionesActivas());

                listaTemporal.add(filaComuna);
            }
        }

        int tamañoComunas = listaTemporal.size();
        if (tamañoComunas == 0) {
            return new String[0][5];
        }

        String[][] resultadoMatriz = new String[tamañoComunas][5];
        for (int i = 0; i < tamañoComunas; i++) {
            resultadoMatriz[i] = listaTemporal.get(i);
        }

        return resultadoMatriz;
    }

    public String[][] listaEstaciones(int codigoRegion, int codigoComuna) {
        Region regionEncontrada = buscarRegion(codigoRegion);
        if (regionEncontrada == null) {
            return new String[0][5];
        }

        Comuna comunaEncontrada = regionEncontrada.findComunaById(codigoComuna);
        if (comunaEncontrada == null) {
            return new String[0][5];
        }

        ArrayList<String[]> listaTemporal = new ArrayList<>();

        for (EstacionMeteorologica estacionActual : this.listaEstaciones) {

            EstacionMeteorologica estacionDeComuna = comunaEncontrada.findEstacionById(estacionActual.getCodigo());

            if (estacionDeComuna == estacionActual) {
                String[] base = estacionActual.toString().split(";", -1);

                String[] filaEstacion = new String[5];
                if (base.length >= 5) {
                    filaEstacion[0] = base[0].trim();
                    filaEstacion[1] = base[1].trim();
                    filaEstacion[2] = base[2].trim();
                    filaEstacion[3] = base[3].trim();
                    filaEstacion[4] = base[4].trim();
                } else {
                    filaEstacion[0] = estacionActual.getCodigo();
                    filaEstacion[1] = estacionActual.getNombre();
                    filaEstacion[2] = "(" + estacionActual.getLatitud() + "; " + estacionActual.getLongitud() + "; " + (int)estacionActual.getAltitud() + " m)";
                    filaEstacion[3] = String.valueOf(estacionActual.getEstado());
                    filaEstacion[4] = String.valueOf(estacionActual.getCantidadSensoresOperativos());
                }

                listaTemporal.add(filaEstacion);
            }
        }

        int tamañoEstaciones = listaTemporal.size();
        if (tamañoEstaciones == 0) {
            return new String[0][5];
        }

        String[][] resultadoMatriz = new String[tamañoEstaciones][5];
        for (int i = 0; i < tamañoEstaciones; i++) {
            resultadoMatriz[i] = listaTemporal.get(i);
        }

        return resultadoMatriz;
    }

    public String[][] listaSensores(String codigoEstacion) {
        EstacionMeteorologica estacionEncontrada = buscarEstacion(codigoEstacion);
        if (estacionEncontrada == null) {
            return new String[0][7];
        }

        return estacionEncontrada.getResumenSensores();
    }

    public String[][] listaMediciones(String codigoEstacion, String codigoSensor, LocalDateTime inicio, LocalDateTime fin) {
        EstacionMeteorologica estacionEncontrada = buscarEstacion(codigoEstacion);
        if (estacionEncontrada == null) {
            return new String[0][4];
        }

        return estacionEncontrada.getMedicionesSensorBetween(codigoSensor, inicio, fin);
    }
}