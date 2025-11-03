package es.tecnalia.ittxartela.ws.server.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EstadoPeticionAsincrona {
    RECIBIDA("0006", "Recibida, estamos en ello"),
    DISPONIBLE("0007", "Respuesta disponible"),
    ERROR("0099", "Error durante el procesamiento");

    private final String codigo;
    private final String descripcion;

    public boolean matches(String codigo) {
        return this.codigo.equalsIgnoreCase(codigo);
    }
}