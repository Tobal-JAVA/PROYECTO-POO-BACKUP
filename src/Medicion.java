import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * Medicion inmutable tomada por un sensor.
 * Autores: [Completar nombres del equipo]
 */
public final class Medicion {
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final LocalDateTime fechaHora;
    private final float valor;

    public Medicion(LocalDateTime fechaHora, float valor) {
        this.fechaHora = Objects.requireNonNull(fechaHora);
        this.valor = valor;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public float getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Medicion)) {
            return false;
        }
        Medicion otra = (Medicion) obj;
        return fechaHora.equals(otra.fechaHora);
    }

    @Override
    public int hashCode() {
        return fechaHora.hashCode();
    }

    @Override
    public String toString() {
        return fechaHora.format(FORMATO) + "; "
                + String.format(Locale.US, "%.1f", valor);
    }
}
