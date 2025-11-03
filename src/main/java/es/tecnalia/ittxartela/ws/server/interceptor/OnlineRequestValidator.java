package es.tecnalia.ittxartela.ws.server.interceptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.XMLConstants;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OnlineRequestValidator extends AbstractPhaseInterceptor<Message> {

	private final Schema schema;

	public OnlineRequestValidator() throws Exception {
		super(Phase.RECEIVE);
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		factory.setResourceResolver(new ClasspathResourceResolver());

		InputStream xsdStream = getClass().getClassLoader().getResourceAsStream("wsdl/online/peticion.xsd");
		if (xsdStream == null) {
			throw new IllegalArgumentException("No se encontró el archivo XSD");
		}
		this.schema = factory.newSchema(new StreamSource(xsdStream));
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		// 1️⃣ Filtrar GET para permitir ?wsdl y descargas
		String httpMethod = (String) message.get(Message.HTTP_REQUEST_METHOD);
		if ("GET".equalsIgnoreCase(httpMethod)) {
			// No validamos los GET (como ?wsdl)
			return;
		}

		try {
			InputStream originalStream = message.getContent(InputStream.class);
			if (originalStream == null) {
				throw new Fault(new IllegalArgumentException("El InputStream del mensaje SOAP es nulo"));
			}

			byte[] soapBytes = IOUtils.readBytesFromStream(originalStream);

			SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null,
					new ByteArrayInputStream(soapBytes));

			SOAPBody body = soapMessage.getSOAPBody();

			Node payload = null;

			for (Node node = body.getFirstChild(); node != null; node = node.getNextSibling()) {
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					payload = node;
					break;
				}
			}

			if (payload == null) {
				throw new Fault(
						new IllegalArgumentException("No se encontró un nodo de tipo ELEMENT en el cuerpo SOAP"));
			}

			schema.newValidator().validate(new DOMSource(payload));

			message.setContent(InputStream.class, new ByteArrayInputStream(soapBytes));

		} catch (Exception e) {
			throw new Fault(new IllegalArgumentException(
					"Error de validación del contenido del SOAP Body: " + e.getMessage(), e));
		}
	}

	private String nodeToString(Node node) throws Exception {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		transformer.transform(new DOMSource(node), new StreamResult(out));
		return out.toString("UTF-8");
	}

	private class ClasspathResourceResolver implements LSResourceResolver {

		@Override
		public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId,
				String baseURI) {
			InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("wsdl/online/" + systemId);
			if (resourceAsStream == null) {
				return null;
			}
			return new SimpleLSInput(publicId, systemId, resourceAsStream);
		}
	}

	private class SimpleLSInput implements LSInput {
		private String publicId;
		private String systemId;
		private InputStream inputStream;

		public SimpleLSInput(String publicId, String systemId, InputStream inputStream) {
			this.publicId = publicId;
			this.systemId = systemId;
			this.inputStream = inputStream;
		}

		@Override
		public Reader getCharacterStream() {
			return null;
		}

		@Override
		public void setCharacterStream(Reader characterStream) {
		}

		@Override
		public InputStream getByteStream() {
			return inputStream;
		}

		@Override
		public void setByteStream(InputStream byteStream) {
			this.inputStream = byteStream;
		}

		@Override
		public String getStringData() {
			return null;
		}

		@Override
		public void setStringData(String stringData) {
		}

		@Override
		public String getSystemId() {
			return systemId;
		}

		@Override
		public void setSystemId(String systemId) {
			this.systemId = systemId;
		}

		@Override
		public String getPublicId() {
			return publicId;
		}

		@Override
		public void setPublicId(String publicId) {
			this.publicId = publicId;
		}

		@Override
		public String getBaseURI() {
			return null;
		}

		@Override
		public void setBaseURI(String baseURI) {
		}

		@Override
		public String getEncoding() {
			return null;
		}

		@Override
		public void setEncoding(String encoding) {
		}

		@Override
		public boolean getCertifiedText() {
			return false;
		}

		@Override
		public void setCertifiedText(boolean certifiedText) {
		}
	}

}
