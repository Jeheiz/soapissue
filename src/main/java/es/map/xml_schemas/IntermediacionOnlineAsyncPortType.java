package es.map.xml_schemas;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(
    targetNamespace = "http://www.map.es/xml-schemas",
    name = "IntermediacionOnlineAsyncPortType"
)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.ObjectFactory.class,
    es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.ObjectFactory.class,
    es.redsara.intermediacion.scsp.esquemas.datosespecificos.ObjectFactory.class
})
public interface IntermediacionOnlineAsyncPortType {



	   @WebMethod(action = "peticionAsincrona")
	    @WebResult(
	        name = "Respuesta",
	        targetNamespace = "http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta",
	        partName = "respuesta"
	    )
	    public es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Respuesta peticionAsincrona(
	        @WebParam(
	            partName = "peticion",
	            name = "Peticion",
	            targetNamespace = "http://intermediacion.redsara.es/scsp/esquemas/V3/peticion"
	        )
	        es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Peticion peticion
	    );

	    @WebMethod(action = "consultarPeticionAsincrona")
	    @WebResult(
	        name = "Respuesta",
	        targetNamespace = "http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta",
	        partName = "respuesta"
	    )
	    public es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Respuesta consultarPeticionAsincrona(
	        @WebParam(
	            partName = "peticion",
	            name = "Peticion",
            targetNamespace = "http://intermediacion.redsara.es/scsp/esquemas/V3/peticion"
        )
        es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Peticion peticion
    );

}