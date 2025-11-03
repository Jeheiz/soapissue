package es.tecnalia.ittxartela.ws.server.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.DatosGenericos;
import es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Peticion;
import es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Procedimiento;
import es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.SolicitudTransmision;
import es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Titular;

@Component
public class PeticionValidator {

    private static final List<String> CODIGOS_AUTORIZADOS = Arrays.asList("PROC_ITTXARTELA", "TEST_DESA_IOP");

    public Optional<RespuestaError> validar(Peticion peticion) {
        SolicitudTransmision solicitud = peticion.getSolicitudes().getSolicitudTransmision().get(0);
        DatosGenericos datos = solicitud.getDatosGenericos();

        // Validación 0254: Información mínima del titular
        Titular titular = datos.getTitular();
        boolean tieneDoc = titular.getTipoDocumentacion() != null && titular.getDocumentacion() != null;
        boolean tieneNombre = titular.getNombre() != null && titular.getApellido1() != null;

        if (!tieneDoc && !tieneNombre) {
            return Optional.of(new RespuestaError("0254", "No se ha aportado la información mínima necesaria para tramitar la petición."));
        }

        // Validación 0314: Código de procedimiento no autorizado
        Procedimiento procedimiento = datos.getSolicitante().getProcedimiento();
        if (procedimiento == null || procedimiento.getCodProcedimiento() == null ||
            !CODIGOS_AUTORIZADOS.contains(procedimiento.getCodProcedimiento())) {
            return Optional.of(new RespuestaError("0314", "El código de procedimiento recibido no tiene autorización para este servicio."));
        }

        // Validación 0401: Estructura inválida (esto normalmente lo lanza CXF automáticamente)
        // Aquí solo se simula una validación manual adicional
        if (datos.getEmisor() == null || datos.getSolicitante() == null || datos.getTransmision() == null) {
            return Optional.of(new RespuestaError("0401", "La estructura del fichero recibido no corresponde con el esquema."));
        }

        return Optional.empty(); // Todo válido
    }

    public static class RespuestaError {
        private final String codigo;
        private final String mensaje;

        public RespuestaError(String codigo, String mensaje) {
            this.codigo = codigo;
            this.mensaje = mensaje;
        }

        public String getCodigo() { return codigo; }
        public String getMensaje() { return mensaje; }
    }
}
