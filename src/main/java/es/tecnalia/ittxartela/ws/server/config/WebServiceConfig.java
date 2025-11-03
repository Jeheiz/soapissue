package es.tecnalia.ittxartela.ws.server.config;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.tecnalia.ittxartela.ws.server.service.ITTxartelaOnlineAsyncServiceImpl;
import es.tecnalia.ittxartela.ws.server.service.ITTxartelaOnlineServiceImpl;

/**
 * Configuración CXF del servicio ITTxartela.
 * Publica los endpoints síncrono (/online) y asíncrono (/onlineAsync)
 * con soporte para los namespaces NISAE (SCSP V3).
 */
@Configuration
public class WebServiceConfig {

    @Autowired
    private Bus bus;

    /**
     * Endpoint del servicio SÍNCRONO (peticionSincrona)
     */
    @Bean
    public Endpoint endpointSync(ITTxartelaOnlineServiceImpl syncImpl) {
        EndpointImpl endpoint = new EndpointImpl(bus, syncImpl);
        endpoint.publish("/ittxartela/online");

        // Servicio síncrono → usa el WSDL base
        endpoint.setServiceName(new QName(
                "http://www.map.es/xml-schemas",
                "IntermediacionOnlinePortType"));

        endpoint.setWsdlLocation("classpath:wsdl/online/x53jiServicioIntermediacion.wsdl");

        return endpoint;
    }

    /**
     * Endpoint del servicio ASÍNCRONO (peticionAsincrona y consultarPeticionAsincrona)
     */
    @Bean
    public Endpoint endpointAsync(ITTxartelaOnlineAsyncServiceImpl asyncImpl) {
        EndpointImpl endpoint = new EndpointImpl(bus, asyncImpl);
        endpoint.publish("/ittxartela/onlineAsync");

        // Servicio asíncrono → usa WSDL separado
        endpoint.setServiceName(new QName(
                "http://www.map.es/xml-schemas",
                "IntermediacionOnlineAsyncPortType"));

        // 🔧 CORRECCIÓN: apuntar al WSDL ASÍNCRONO
        endpoint.setWsdlLocation("classpath:wsdl/online/x53jiServicioIntermediacion.wsdl");

        return endpoint;
    }
}
