package es.tecnalia.ittxartela.ws.server.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlUtil {

   
        public static String toXml(Object objeto) {

                if (objeto == null) {
                        throw new IllegalArgumentException("El objeto no puede ser null");
                }

                StringWriter writer = new StringWriter();

                try {

                        JAXBContext context = JAXBContext.newInstance(objeto.getClass());
                        Marshaller marshaller = context.createMarshaller();
                        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                        marshaller.marshal(objeto, writer);

                } catch (JAXBException e) {

                        writer.write("<error al convertir a XML: " + e.getMessage() + ">");
                }

                return writer.toString();
        }

        public static String obtenerFechaHoraXml() {

                ZonedDateTime ahora = ZonedDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

                return ahora.format(formatter);
        }

        public static <T> T fromXml(String xml, Class<T> clazz) {
                if (xml == null || xml.isBlank()) {
                        throw new IllegalArgumentException("El contenido XML no puede ser nulo o vacío");
                }

                try {
                        JAXBContext context = JAXBContext.newInstance(clazz);
                        Unmarshaller unmarshaller = context.createUnmarshaller();
                        Object resultado = unmarshaller.unmarshal(new StringReader(xml));
                        return clazz.cast(resultado);
                } catch (JAXBException e) {
                        throw new IllegalArgumentException("No ha sido posible convertir el XML al tipo " + clazz.getSimpleName(), e);
                }
        }
}