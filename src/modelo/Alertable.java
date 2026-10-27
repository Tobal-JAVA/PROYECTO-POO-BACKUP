package modelo;

import java.util.Optional;

public interface Alertable {
     Optional<NivelAlerta> evaluaAlerta(float valor);
     String getFenomeno(float valo);
     default boolean generaAlerta(float valor){
         return evaluaAlerta(valor).isPresent();
     }
}
