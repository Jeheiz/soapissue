package es.tecnalia.ittxartela.ws.server.service;

import java.util.Optional;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Peticion;
import es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Respuesta;
import es.tecnalia.ittxartela.ws.server.constant.EstadoPeticionAsincrona;
import es.tecnalia.ittxartela.ws.server.model.AsyncPeticion;
import es.tecnalia.ittxartela.ws.server.repository.AsyncPeticionRepository;
import es.tecnalia.ittxartela.ws.server.util.PeticionValidator;
import es.tecnalia.ittxartela.ws.server.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@WebService(
    serviceName = "x53jiServicioOnlineIntermediacion",
    portName = "IntermediacionOnlineAsyncPort",
    targetNamespace = "http://www.map.es/xml-schemas",
    endpointInterface = "es.map.xml_schemas.IntermediacionOnlineAsyncPortType"
)
public class ITTxartelaOnlineAsyncServiceImpl implements es.map.xml_schemas.IntermediacionOnlineAsyncPortType {


    @Autowired
    private PeticionValidator peticionValidator;

    @Autowired
    private AsyncPeticionRepository asyncPeticionRepository;

    @Override
    @Transactional
    @WebMethod(operationName = "peticionAsincrona")
    @WebResult(name = "respuesta")
    public Respuesta peticionAsincrona(@WebParam(name = "peticion") Peticion peticion) {
        log.info("Petición ASINCRONA recibida {}", peticion.getAtributos().getIdPeticion());

        Optional<PeticionValidator.RespuestaError> error = peticionValidator.validar(peticion);

        Respuesta respuesta = new Respuesta();
        es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Atributos atributos =
                new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Atributos();
        respuesta.setAtributos(atributos);
        atributos.setIdPeticion(peticion.getAtributos().getIdPeticion());
        atributos.setTimeStamp(XmlUtil.obtenerFechaHoraXml());

        es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Estado estado =
                new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Estado();
        atributos.setEstado(estado);

        if (error.isPresent()) {
            estado.setCodigoEstado(EstadoPeticionAsincrona.ERROR.getCodigo());
            estado.setLiteralError(error.get().getMensaje());
            estado.setTiempoEstimadoRespuesta(0);
            atributos.setNumElementos(0);
            return respuesta;
        }

        // Registrar petición en tabla async_peticion como RECIBIDA
        AsyncPeticion ap = new AsyncPeticion();
        ap.setIdPeticion(peticion.getAtributos().getIdPeticion());
        ap.setXmlPeticion(XmlUtil.toXml(peticion));
        ap.setEstado(EstadoPeticionAsincrona.RECIBIDA.getCodigo());
        ap.setTer(0);
        asyncPeticionRepository.save(ap);

        // Respuesta inmediata RECIBIDA
        estado.setCodigoEstado(EstadoPeticionAsincrona.RECIBIDA.getCodigo());
        estado.setLiteralError(EstadoPeticionAsincrona.RECIBIDA.getDescripcion());
        estado.setTiempoEstimadoRespuesta(5);
        atributos.setNumElementos(0);
        return respuesta;
    }

    @Override
    public Respuesta consultarPeticionAsincrona(Peticion peticion) {
        log.info("Consulta ASÍNCRONA recibida. ID: {}", peticion.getAtributos().getIdPeticion());

        String id = peticion.getAtributos().getIdPeticion();
        AsyncPeticion ap = asyncPeticionRepository.findByIdPeticion(id);

        if (ap == null) {
            Respuesta r = new Respuesta();
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Atributos at = new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Atributos();
            r.setAtributos(at);
            at.setIdPeticion(id);
            at.setTimeStamp(XmlUtil.obtenerFechaHoraXml());
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Estado est = new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Estado();
            est.setCodigoEstado("0009");
            est.setLiteralError("Petición inexistente");
            est.setTiempoEstimadoRespuesta(0);
            at.setEstado(est);
            at.setNumElementos(0);
            return r;
        }

        if (EstadoPeticionAsincrona.DISPONIBLE.matches(ap.getEstado()) && ap.getXmlRespuesta() != null) {
            return XmlUtil.fromXml(ap.getXmlRespuesta(), Respuesta.class);
        }

        Respuesta r = new Respuesta();
        es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Atributos at = new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Atributos();
        r.setAtributos(at);
        at.setIdPeticion(id);
        at.setTimeStamp(XmlUtil.obtenerFechaHoraXml());
        es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Estado est = new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Estado();
        est.setCodigoEstado(EstadoPeticionAsincrona.RECIBIDA.getCodigo());
        est.setLiteralError(EstadoPeticionAsincrona.RECIBIDA.getDescripcion());
        est.setTiempoEstimadoRespuesta(5);
        at.setEstado(est);
        at.setNumElementos(0);
        return r;
    }
}
