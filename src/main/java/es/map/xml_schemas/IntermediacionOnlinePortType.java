package es.map.xml_schemas;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;



@WebService(targetNamespace = "http://www.map.es/xml-schemas", name = "IntermediacionOnlinePortType")
@XmlSeeAlso({es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.ObjectFactory.class, es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.ObjectFactory.class, es.redsara.intermediacion.scsp.esquemas.datosespecificos.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface IntermediacionOnlinePortType {

	@WebMethod(action = "peticionSincrona")
	@WebResult(name = "Respuesta", targetNamespace = "http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta", partName = "respuesta")
	public es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Respuesta peticionSincrona(

			@WebParam(partName = "peticion", name = "Peticion", targetNamespace = "http://intermediacion.redsara.es/scsp/esquemas/V3/peticion") es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Peticion peticion);

}
