package es.tecnalia.ittxartela.ws.server.service;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Peticion;
import es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Estado;
import es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Respuesta;
import es.tecnalia.ittxartela.ws.server.model.Audit;
import es.tecnalia.ittxartela.ws.server.model.Matricula;
import es.tecnalia.ittxartela.ws.server.repository.AuditRepository;
import es.tecnalia.ittxartela.ws.server.repository.MatriculaRepository;
import es.tecnalia.ittxartela.ws.server.util.XmlUtil;
import es.tecnalia.ittxartela.ws.server.util.PeticionValidator;
import es.tecnalia.ittxartela.ws.server.util.DocumentoValidator;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@WebService(
    serviceName = "ITTxartelaOnlineServiceImplService",
    portName = "ITTxartelaOnlineServiceImplPort",
    targetNamespace = "http://www.map.es/xml-schemas",
    endpointInterface = "es.map.xml_schemas.IntermediacionOnlinePortType"
)
public class ITTxartelaOnlineServiceImpl implements es.map.xml_schemas.IntermediacionOnlinePortType {
    private static final Duration TIEMPO_RESPUESTA_POR_DEFECTO = Duration.ofDays(5);
    private static final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private PeticionValidator peticionValidator;
    @Autowired
    private AuditRepository auditRepository;
    @Autowired
    private MatriculaRepository matriculaRepository;
    // ==============================================================
    // SERVICIO SÍNCRONO → /ittxartela/online
    // ==============================================================
    @Override
    @Transactional
    @WebMethod(operationName = "peticionSincrona")
    @WebResult(name = "respuesta")
    public Respuesta peticionSincrona(@WebParam(name = "peticion") Peticion peticion) {
        log.info("Petición SINCRONA recibida {}", XmlUtil.toXml(peticion));
        Audit audit = crearRegistroAuditoria(peticion, LocalDateTime.now());
        Respuesta respuesta = generarRespuestaValidada(peticion);
        audit.setXmlRespuesta(XmlUtil.toXml(respuesta));
        audit.setEstado("0000");
        audit.setMensajeEstado("Petición tramitada correctamente");
        audit.setFechaDisponible(LocalDateTime.now());
        auditRepository.save(audit);
        log.info("Petición SINCRONA procesada correctamente");
        return respuesta;
    }
    // ==============================================================
    // MÉTODOS AUXILIARES
    // ==============================================================
    private Audit crearRegistroAuditoria(Peticion peticion, LocalDateTime fechaPrevista) {
        Audit audit = new Audit();
        audit.setIdPeticion(peticion.getAtributos().getIdPeticion());
        audit.setXmlPeticion(XmlUtil.toXml(peticion));
        audit.setEstado("0000");
        audit.setMensajeEstado("Petición tramitada correctamente");
        audit.setFechaRespuestaPrevista(fechaPrevista);
        return auditRepository.save(audit);
    }
    private Respuesta generarRespuestaValidada(Peticion peticion) {
        Respuesta respuesta = construirRespuestaBase(peticion);
        Optional<PeticionValidator.RespuestaError> error = peticionValidator.validar(peticion);
        es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Atributos atributos = respuesta.getAtributos();
        if (atributos == null) {
            atributos = new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Atributos();
            respuesta.setAtributos(atributos);
        }
        if (error.isPresent()) {
            aplicarError(respuesta, peticion, error.get());
            atributos.setNumElementos(0);
            return respuesta;
        }
        es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.SolicitudTransmision st =
                peticion.getSolicitudes().getSolicitudTransmision().get(0);
        // DNI y parámetros
        String dni = Optional.ofNullable(st.getDatosEspecificos())
                .map(es.redsara.intermediacion.scsp.esquemas.datosespecificos.DatosEspecificosItTxartela::getConsulta)
                .map(es.redsara.intermediacion.scsp.esquemas.datosespecificos.Consulta::getDocumentacion)
                .orElse(st.getDatosGenericos().getTitular().getDocumentacion());
        dni = (dni != null ? dni.trim().toUpperCase() : null);
        Integer tipoCertificacion = Optional.ofNullable(st.getDatosEspecificos())
                .map(es.redsara.intermediacion.scsp.esquemas.datosespecificos.DatosEspecificosItTxartela::getConsulta)
                .map(es.redsara.intermediacion.scsp.esquemas.datosespecificos.Consulta::getTipoCertificacion)
                .orElse(null);
        if (Integer.valueOf(0).equals(tipoCertificacion)) {
            tipoCertificacion = null;
        }
        String fechaLimiteStr = Optional.ofNullable(st.getDatosEspecificos())
                .map(es.redsara.intermediacion.scsp.esquemas.datosespecificos.DatosEspecificosItTxartela::getConsulta)
                .map(es.redsara.intermediacion.scsp.esquemas.datosespecificos.Consulta::getFechaLimite)
                .orElse(null);
        // Validaciones SCSP
        boolean formatoDniValido = dni != null && DocumentoValidator.esDocumentoValido(dni);
        boolean tipoCertValido = (tipoCertificacion == null || tipoCertificacion == 1 || tipoCertificacion == 2);
        Date fechaLimite = null;
        boolean fechaValida = true;
        if (fechaLimiteStr != null && !fechaLimiteStr.trim().isEmpty()) {
            try {
                java.time.LocalDate d = java.time.LocalDate.parse(fechaLimiteStr.trim());
                fechaLimite = java.util.Date.from(d.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
            } catch (Exception e) {
                fechaValida = false;
            }
        } else {
            fechaLimite = new Date(System.currentTimeMillis());
        }
        Estado estado = asegurarEstado(respuesta);
        estado.setCodigoEstado("0000");
        estado.setLiteralError("Operación correcta");
        estado.setTiempoEstimadoRespuesta(0);
        if (!formatoDniValido) {
            return construirRespuestaSinDatos(respuesta, st, dni, "ERROR", "No se ha especificado un NIF/NIE válido. Formato 99999999X");
        }
        if (!tipoCertValido) {
            return construirRespuestaSinDatos(respuesta, st, dni, "ERROR", "Tipo de certificación no válido");
        }
        if (!fechaValida) {
            return construirRespuestaSinDatos(respuesta, st, dni, "ERROR", "No se ha especificado una fecha límite válida. Formato dd/MM/yyyy");
        }
        // Fecha límite fin de día
        if (fechaLimite != null) {
            java.util.Date endOfDay = java.util.Date.from(
                    fechaLimite.toInstant()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                            .atTime(java.time.LocalTime.MAX)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toInstant());
            fechaLimite = new Date(endOfDay.getTime());
        }
        log.info("SYNC query -> dni={} tipoCertificacion={} fechaLimite(EOD)={}", dni, tipoCertificacion, fechaLimite);
        java.sql.Timestamp fechaLimiteTs = fechaLimite != null
                ? new java.sql.Timestamp(fechaLimite.getTime())
                : new java.sql.Timestamp(System.currentTimeMillis());
        List<Matricula> certificaciones =
                matriculaRepository.findCertificacionesValidas(dni, tipoCertificacion, fechaLimiteTs);
        es.redsara.intermediacion.scsp.esquemas.datosespecificos.DatosEspecificosItTxartela datosEspecificos =
                new es.redsara.intermediacion.scsp.esquemas.datosespecificos.DatosEspecificosItTxartela();
        es.redsara.intermediacion.scsp.esquemas.datosespecificos.Traza traza =
                new es.redsara.intermediacion.scsp.esquemas.datosespecificos.Traza();
        traza.setIdPeticion(peticion.getAtributos().getIdPeticion());
        traza.setIdTraza(st.getDatosEspecificos() != null && st.getDatosEspecificos().getConsulta() != null
                ? st.getDatosEspecificos().getConsulta().getIdTraza() : "");
        es.redsara.intermediacion.scsp.esquemas.datosespecificos.EstadoResultado estadoResultado =
                new es.redsara.intermediacion.scsp.esquemas.datosespecificos.EstadoResultado();
        if (certificaciones.isEmpty()) {
            estadoResultado.setResultado("ERROR");
            estadoResultado.setDescripcion("No se han encontrado datos para los parametros de entrada indicados");
        } else {
            estadoResultado.setResultado("OK");
            estadoResultado.setDescripcion("Certificaciones aprobadas encontradas");
        }
        es.redsara.intermediacion.scsp.esquemas.datosespecificos.Persona persona =
                new es.redsara.intermediacion.scsp.esquemas.datosespecificos.Persona();
        persona.setDocumentacion(dni);
        if (!certificaciones.isEmpty()) {
            es.redsara.intermediacion.scsp.esquemas.datosespecificos.Certificados certificados =
                    new es.redsara.intermediacion.scsp.esquemas.datosespecificos.Certificados();
            certificados.getCertificado().addAll(certificaciones.stream().map(m -> {
                es.redsara.intermediacion.scsp.esquemas.datosespecificos.Certificado c =
                        new es.redsara.intermediacion.scsp.esquemas.datosespecificos.Certificado();
                c.setCodigoModulo(String.valueOf(m.getCod_modulo()));
                c.setFechaExamen(formatFechaExamen(m.getFecha_exam()));
                return c;
            }).collect(Collectors.toList()));
            persona.setCertificados(certificados);
        }
        es.redsara.intermediacion.scsp.esquemas.datosespecificos.Personas personas =
                new es.redsara.intermediacion.scsp.esquemas.datosespecificos.Personas();
        personas.getPersona().add(persona);
        es.redsara.intermediacion.scsp.esquemas.datosespecificos.Retorno retorno =
                new es.redsara.intermediacion.scsp.esquemas.datosespecificos.Retorno();
        retorno.setDatosTraza(traza);
        retorno.setEstadoResultado(estadoResultado);
        retorno.setPersonas(personas);
        es.redsara.intermediacion.scsp.esquemas.datosespecificos.Consulta consultaOriginal =
                (st.getDatosEspecificos() != null && st.getDatosEspecificos().getConsulta() != null)
                        ? st.getDatosEspecificos().getConsulta()
                        : null;
        if (consultaOriginal != null) {
            datosEspecificos.setConsulta(consultaOriginal);
        }
        datosEspecificos.setRetorno(retorno);
        es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.TransmisionDatos transmision =
                new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.TransmisionDatos();
        transmision.setDatosGenericos(mapper.map(st.getDatosGenericos(),
                es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.DatosGenericos.class));
        transmision.setDatosEspecificos(datosEspecificos);
        es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Transmisiones transmisiones =
                new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Transmisiones();
        transmisiones.getTransmisionDatos().add(transmision);
        respuesta.setTransmisiones(transmisiones);
        estado.setCodigoEstado("0000");
        estado.setLiteralError("Operacion correcta");
        atributos.setNumElementos(1);
        return respuesta;
    }
    private String formatFechaExamen(Date fecha) {
        if (fecha == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(fecha);
    }
    private Respuesta construirRespuestaSinDatos(
            Respuesta respuestaBase,
            es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.SolicitudTransmision st,
            String dni,
            String codigoResultado,
            String descripcion) {
        Respuesta respuesta = respuestaBase;
        es.redsara.intermediacion.scsp.esquemas.datosespecificos.DatosEspecificosItTxartela datosEspecificos =
                new es.redsara.intermediacion.scsp.esquemas.datosespecificos.DatosEspecificosItTxartela();
        es.redsara.intermediacion.scsp.esquemas.datosespecificos.Traza traza =
                new es.redsara.intermediacion.scsp.esquemas.datosespecificos.Traza();
        traza.setIdPeticion(respuesta.getAtributos().getIdPeticion());
        traza.setIdTraza(st.getDatosEspecificos() != null && st.getDatosEspecificos().getConsulta() != null
                ? st.getDatosEspecificos().getConsulta().getIdTraza() : "");
        es.redsara.intermediacion.scsp.esquemas.datosespecificos.EstadoResultado estadoResultado =
                new es.redsara.intermediacion.scsp.esquemas.datosespecificos.EstadoResultado();
        estadoResultado.setResultado(codigoResultado);
        estadoResultado.setDescripcion(descripcion);
        es.redsara.intermediacion.scsp.esquemas.datosespecificos.Persona persona =
                new es.redsara.intermediacion.scsp.esquemas.datosespecificos.Persona();
        persona.setDocumentacion(dni);
        es.redsara.intermediacion.scsp.esquemas.datosespecificos.Personas personas =
                new es.redsara.intermediacion.scsp.esquemas.datosespecificos.Personas();
        personas.getPersona().add(persona);
        es.redsara.intermediacion.scsp.esquemas.datosespecificos.Retorno retorno =
                new es.redsara.intermediacion.scsp.esquemas.datosespecificos.Retorno();
        retorno.setDatosTraza(traza);
        retorno.setEstadoResultado(estadoResultado);
        retorno.setPersonas(personas);
        // Copy the original Consulta (with TipoCertificacion) into the response
        es.redsara.intermediacion.scsp.esquemas.datosespecificos.Consulta consultaOriginal =
                (st.getDatosEspecificos() != null && st.getDatosEspecificos().getConsulta() != null)
                        ? st.getDatosEspecificos().getConsulta()
                        : null;
        if (consultaOriginal != null) {
            datosEspecificos.setConsulta(consultaOriginal);
        }
        datosEspecificos.setRetorno(retorno);
        es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.TransmisionDatos transmision =
                new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.TransmisionDatos();
        transmision.setDatosGenericos(mapper.map(st.getDatosGenericos(),
                es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.DatosGenericos.class));
        transmision.setDatosEspecificos(datosEspecificos);
        // ✅ evitar duplicación JAXB
        es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Transmisiones transmisiones =
                new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Transmisiones();
        transmisiones.getTransmisionDatos().clear();
        transmisiones.getTransmisionDatos().add(transmision);
        respuesta.setTransmisiones(transmisiones);
        respuesta.getAtributos().setNumElementos(1);
        Estado estado = asegurarEstado(respuesta);
        estado.setCodigoEstado("0000");
        estado.setLiteralError("Operación correcta");
        return respuesta;
    }
    private Respuesta construirRespuestaBase(Peticion peticion) {
        Respuesta respuesta = mapper.map(peticion, Respuesta.class);
        if (respuesta.getAtributos() == null) {
            respuesta.setAtributos(new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Atributos());
        }
        respuesta.getAtributos().setTimeStamp(XmlUtil.obtenerFechaHoraXml());
        asegurarEstado(respuesta);
        return respuesta;
    }
    private Estado asegurarEstado(Respuesta respuesta) {
        if (respuesta.getAtributos().getEstado() == null) {
            respuesta.getAtributos().setEstado(new Estado());
        }
        return respuesta.getAtributos().getEstado();
    }
    private void aplicarError(Respuesta respuesta, Peticion peticion, PeticionValidator.RespuestaError error) {
        Estado estado = asegurarEstado(respuesta);
        estado.setCodigoEstado("0008");
        estado.setLiteralError(error.getMensaje());
        estado.setTiempoEstimadoRespuesta(0);
    }
}
