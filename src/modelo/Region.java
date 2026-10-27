package modelo;

import java.util.ArrayList;
import java.util.Optional;
/**
 * @author Beatriz Aguilera / Modificado Noelia Montecinos
 */
public class Region {
    private int codigo;
    private String nombre;
    private ArrayList<Comuna> comunas;

    public Region(int cod, String nom) {
        this.codigo = cod;
        this.nombre = nom;
        this.comunas = new ArrayList<>();
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean addComuna(int cod, String nom) {
        for (Comuna c : comunas) {
            if (c.getCodigo() == cod || c.getNombre().equalsIgnoreCase(nom)) {
                return false;
            }
        }

        Comuna nuevaComuna = new Comuna(cod, nom, this);
        comunas.add(nuevaComuna);
        return true;
    }

    public Optional<Comuna> findComunaById(int codigo) {
        for (Comuna c : comunas) {
            if (c.getCodigo() == codigo) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
    public Comuna[] getComunas() {
        return comunas.toArray(new Comuna[0]);
    }

    public int getCantidadEstaciones() {
        int total = 0;
        for (Comuna c : comunas) {
            total += c.getCantidadEstaciones();
        }
        return total;
    }
}
