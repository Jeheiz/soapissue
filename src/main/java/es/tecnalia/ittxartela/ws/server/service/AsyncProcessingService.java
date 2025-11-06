package es.tecnalia.ittxartela.ws.server.service;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import es.redsara.intermediacion.scsp.esquemas.datosespecificos.Certificado;
import es.redsara.intermediacion.scsp.esquemas.datosespecificos.Certificados;
import es.redsara.intermediacion.scsp.esquemas.datosespecificos.DatosEspecificosItTxartela;
import es.redsara.intermediacion.scsp.esquemas.datosespecificos.EstadoResultado;
import es.redsara.intermediacion.scsp.esquemas.datosespecificos.Persona;
import es.redsara.intermediacion.scsp.esquemas.datosespecificos.Personas;
import es.redsara.intermediacion.scsp.esquemas.datosespecificos.Traza;
import es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Peticion;
import es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.SolicitudTransmision;
import es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Respuesta;
import es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.TransmisionDatos;
import es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Transmisiones;
import es.tecnalia.ittxartela.ws.server.constant.EstadoPeticionAsincrona;
import es.tecnalia.ittxartela.ws.server.model.AsyncPeticion;
import es.tecnalia.ittxartela.ws.server.model.Audit;
import es.tecnalia.ittxartela.ws.server.model.Matricula;
import es.tecnalia.ittxartela.ws.server.repository.AsyncPeticionRepository;
import es.tecnalia.ittxartela.ws.server.repository.AuditRepository;
import es.tecnalia.ittxartela.ws.server.repository.AlumnoRepository;
import es.tecnalia.ittxartela.ws.server.repository.MatriculaRepository;
import es.tecnalia.ittxartela.ws.server.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class AsyncProcessingService {

    @Autowired
    private AsyncPeticionRepository asyncPeticionRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AlumnoRepository alumnoRepository;
    
    @Transactional
    @Scheduled(fixedDelayString = "${ittxartela.async.polling-interval-ms:60000}")
    public void procesarPendientes() {
        List<AsyncPeticion> pendientes = asyncPeticionRepository
                .findByEstadoOrderByIdAsc(EstadoPeticionAsincrona.RECIBIDA.getCodigo());
        if (pendientes.isEmpty()) {
            log.debug("[ASYNC] No hay peticiones pendientes de procesar");
            return;
        }

        log.info("[ASYNC] Procesando {} peticiones pendientes", pendientes.size());

        for (AsyncPeticion ap : pendientes) {
            try {
                log.info("[ASYNC] Construyendo respuesta para idPeticion={}", ap.getIdPeticion());

                Respuesta respuesta = construirRespuestaDesdePeticion(ap.getXmlPeticion());
                ap.setXmlRespuesta(XmlUtil.toXml(respuesta));
                ap.setEstado(EstadoPeticionAsincrona.DISPONIBLE.getCodigo());
                asyncPeticionRepository.save(ap);

                log.info("[ASYNC] Petición id={} marcada como DISPONIBLE", ap.getIdPeticion());

                Audit audit = new Audit();
                audit.setIdPeticion(extractIdPeticion(ap.getXmlPeticion()));
                audit.setXmlPeticion(ap.getXmlPeticion());
                audit.setXmlRespuesta(ap.getXmlRespuesta());
                audit.setEstado(EstadoPeticionAsincrona.DISPONIBLE.getCodigo());
                audit.setMensajeEstado(EstadoPeticionAsincrona.DISPONIBLE.getDescripcion());
                audit.setFechaRespuestaPrevista(java.time.LocalDateTime.now());
                audit.setFechaDisponible(java.time.LocalDateTime.now());
                auditRepository.save(audit);

                log.info("[ASYNC] Procesada petición ASYNC {} -> DISPONIBLE", ap.getIdPeticion());

            } catch (Exception e) {
                log.error("[ASYNC] Error procesando petición {}", ap.getIdPeticion(), e);
                ap.setEstado(EstadoPeticionAsincrona.ERROR.getCodigo());
                asyncPeticionRepository.save(ap);
            }
        }
    }

    private Respuesta construirRespuestaDesdePeticion(String xmlPeticion) {
        Peticion peticion = XmlUtil.fromXml(xmlPeticion, Peticion.class);

        Respuesta respuesta = new Respuesta();
        es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Atributos atributos =
                new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Atributos();
        respuesta.setAtributos(atributos);
        atributos.setIdPeticion(peticion.getAtributos().getIdPeticion());
        atributos.setTimeStamp(XmlUtil.obtenerFechaHoraXml());

        es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Estado estado =
                new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Estado();
        atributos.setEstado(estado);

        Transmisiones transmisiones = new Transmisiones();

        List<SolicitudTransmision> solicitudes = peticion.getSolicitudes().getSolicitudTransmision();
        for (SolicitudTransmision st : solicitudes) {
            String dni = st.getDatosGenericos().getTitular().getDocumentacion();
            dni = (dni != null ? dni.trim().toUpperCase() : null);

            Integer tipoCertificacionValida = Optional.ofNullable(st.getDatosEspecificos())
                    .map(es.redsara.intermediacion.scsp.esquemas.datosespecificos.DatosEspecificosItTxartela::getConsulta)
                    .map(es.redsara.intermediacion.scsp.esquemas.datosespecificos.Consulta::getTipoCertificacion)
                    .filter(tc -> tc != null && (tc == 1 || tc == 2))
                    .orElse(null);


            // Obtener y ajustar fecha límite
            String fechaLimiteStr = Optional.ofNullable(st.getDatosEspecificos())
                    .map(es.redsara.intermediacion.scsp.esquemas.datosespecificos.DatosEspecificosItTxartela::getConsulta)
                    .map(es.redsara.intermediacion.scsp.esquemas.datosespecificos.Consulta::getFechaLimite)
                    .orElse(null);

            Date fechaLimite = parseFechaLimite(fechaLimiteStr);

            Date endOfDay = null;
            if (fechaLimite != null) {
                java.time.LocalDateTime eod = fechaLimite.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate()
                        .atTime(java.time.LocalTime.MAX);
                endOfDay = Date.from(eod.atZone(java.time.ZoneId.systemDefault()).toInstant());
            }

            log.info("[ASYNC] Ejecutando consulta -> DNI={}, tipoCertificacion={}, fechaLimite(EOD)={}",
                    dni, tipoCertificacionValida, endOfDay);

            List<Matricula> certificaciones =
                    matriculaRepository.findCertificacionesValidas(dni, tipoCertificacionValida, endOfDay);

            log.info("[ASYNC] Resultados -> {} registros encontrados", certificaciones.size());

            // 🧠 Log detallado de filas obtenidas
            certificaciones.forEach(m ->
                    log.info("[ASYNC] Registro -> Modulo={}, FechaExam={}, Plataforma={}",
                            m.getCod_modulo(), m.getFecha_exam(), m.getPlataforma()));

            DatosEspecificosItTxartela datosEspecificos = new DatosEspecificosItTxartela();
            Traza traza = new Traza();
            traza.setIdPeticion(peticion.getAtributos().getIdPeticion());
            traza.setIdTraza(st.getDatosEspecificos() != null &&
                             st.getDatosEspecificos().getConsulta() != null
                    ? st.getDatosEspecificos().getConsulta().getIdTraza()
                    : "");

            EstadoResultado estadoResultado = new EstadoResultado();

            boolean dniExiste = alumnoRepository.existsByDni(dni);
            if (certificaciones.isEmpty() && !dniExiste) {
                estadoResultado.setResultado("ERROR");
                estadoResultado.setDescripcion("Datos no encontrados");
            } else if (certificaciones.isEmpty()) {
                estadoResultado.setResultado("OK");
                estadoResultado.setDescripcion("Sin certificaciones aprobadas");
            } else {
                estadoResultado.setResultado("OK");
                estadoResultado.setDescripcion("Certificaciones aprobadas encontradas");
            }

            Certificados certificados = new Certificados();
            certificados.getCertificado().addAll(
                    certificaciones.stream().map(m -> {
                        Certificado c = new Certificado();
                        c.setCodigoModulo(String.valueOf(m.getCod_modulo()));
                        c.setFechaExamen(m.getFecha_exam() != null
                                ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(m.getFecha_exam())
                                : null);
                        return c;
                    }).collect(Collectors.toList())
            );

            log.info("[ASYNC] Certificados generados -> {}", certificados.getCertificado().size());

            Persona persona = new Persona();
            persona.setDocumentacion(dni);
            if (!certificaciones.isEmpty()) {
                persona.setCertificados(certificados);
            }

            Personas personas = new Personas();
            personas.getPersona().add(persona);

            es.redsara.intermediacion.scsp.esquemas.datosespecificos.Retorno retorno =
                    new es.redsara.intermediacion.scsp.esquemas.datosespecificos.Retorno();
            retorno.setDatosTraza(traza);
            retorno.setEstadoResultado(estadoResultado);
            retorno.setPersonas(personas);
            datosEspecificos.setRetorno(retorno);

            TransmisionDatos transmision = new TransmisionDatos();
            transmision.setDatosGenericos(mapper.map(st.getDatosGenericos(),
                    es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.DatosGenericos.class));
            transmision.setDatosEspecificos(datosEspecificos);

            transmisiones.getTransmisionDatos().add(transmision);
        }

        respuesta.setTransmisiones(transmisiones);
        estado.setCodigoEstado(EstadoPeticionAsincrona.DISPONIBLE.getCodigo());
        estado.setLiteralError(EstadoPeticionAsincrona.DISPONIBLE.getDescripcion());
        estado.setTiempoEstimadoRespuesta(0);
        atributos.setNumElementos(transmisiones.getTransmisionDatos().size());

        return respuesta;
    }

    private static Date parseFechaLimite(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            return new Date(System.currentTimeMillis());
        }
        String f = fecha.trim();
        try {
            java.time.LocalDate d = java.time.LocalDate.parse(f);
            return java.util.Date.from(d.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        } catch (Exception ignore) { }
        try {
            java.time.LocalDateTime dt = java.time.LocalDateTime.parse(f);
            return java.util.Date.from(dt.atZone(java.time.ZoneId.systemDefault()).toInstant());
        } catch (Exception ignore) { }
        try {
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            java.time.LocalDate d = java.time.LocalDate.parse(f, fmt);
            return java.util.Date.from(d.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        } catch (Exception ignore) { }
        return new Date(System.currentTimeMillis());
    }

    private static String extractIdPeticion(String xml) {
        try {
            Peticion p = XmlUtil.fromXml(xml, Peticion.class);
            return p.getAtributos().getIdPeticion();
        } catch (Exception e) {
            return null;
        }
    }
}

