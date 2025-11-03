package es.map.xml_schemas;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;


@WebServiceClient(name = "x53jiServicioOnlineIntermediacion", wsdlLocation = "classpath:wsdl/online/x53jiServicioIntermediacion.wsdl", targetNamespace = "http://www.map.es/xml-schemas")
public class X53JiServicioOnlineIntermediacion extends Service {

	public static final URL WSDL_LOCATION;

	public static final QName SERVICE = new QName("http://www.map.es/xml-schemas", "x53jiServicioOnlineIntermediacion");
	public static final QName IntermediacionOnlinePort = new QName("http://www.map.es/xml-schemas",
			"IntermediacionOnlinePort");
	static {
		URL url = X53JiServicioOnlineIntermediacion.class.getClassLoader()
				.getResource("wsdl/online/x53jiServicioIntermediacion.wsdl");
		if (url == null) {
			java.util.logging.Logger.getLogger(X53JiServicioOnlineIntermediacion.class.getName()).log(
					java.util.logging.Level.INFO, "Can not initialize the default wsdl from {0}",
					"classpath:wsdl/online/x53jiServicioIntermediacion.wsdl");
		}
		WSDL_LOCATION = url;
	}

	public X53JiServicioOnlineIntermediacion(URL wsdlLocation) {
		super(wsdlLocation, SERVICE);
	}

	public X53JiServicioOnlineIntermediacion(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public X53JiServicioOnlineIntermediacion() {
		super(WSDL_LOCATION, SERVICE);
	}

	public X53JiServicioOnlineIntermediacion(WebServiceFeature... features) {
		super(WSDL_LOCATION, SERVICE, features);
	}

	public X53JiServicioOnlineIntermediacion(URL wsdlLocation, WebServiceFeature... features) {
		super(wsdlLocation, SERVICE, features);
	}

	public X53JiServicioOnlineIntermediacion(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
		super(wsdlLocation, serviceName, features);
	}

	/**
	 *
	 * @return returns IntermediacionOnlinePortType
	 */
	@WebEndpoint(name = "IntermediacionOnlinePort")
	public IntermediacionOnlinePortType getIntermediacionOnlinePort() {
		return super.getPort(IntermediacionOnlinePort, IntermediacionOnlinePortType.class);
	}

	/**
	 *
	 * @param {@link javax.xml.ws.WebServiceFeature} para configurar
	 *                  proxy. 
	 * @return returns IntermediacionOnlinePortType
	 */
	@WebEndpoint(name = "IntermediacionOnlinePort")
	public IntermediacionOnlinePortType getIntermediacionOnlinePort(WebServiceFeature... features) {
		return super.getPort(IntermediacionOnlinePort, IntermediacionOnlinePortType.class, features);
	}

}
