
package es.redsara.intermediacion.scsp.esquemas.datosespecificos;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.redsara.intermediacion.scsp.esquemas.datosespecificos package.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DatosEspecificos_QNAME = new QName(
            "http://intermediacion.redsara.es/scsp/esquemas/datosespecificos", "DatosEspecificos");
    private final static QName _DatosTraza_QNAME = new QName(
            "http://intermediacion.redsara.es/scsp/esquemas/datosespecificos", "DatosTraza");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.redsara.intermediacion.scsp.esquemas.datosespecificos
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DatosEspecificosItTxartela }
     */
    public DatosEspecificosItTxartela createDatosEspecificosItTxartela() {
        return new DatosEspecificosItTxartela();
    }

    /**
     * Create an instance of {@link Consulta }
     */
    public Consulta createConsulta() {
        return new Consulta();
    }

    /**
     * Create an instance of {@link Traza }
     */
    public Traza createTraza() {
        return new Traza();
    }

    /**
     * Create an instance of {@link EstadoResultado }
     */
    public EstadoResultado createEstadoResultado() {
        return new EstadoResultado();
    }

    /**
     * Create an instance of {@link Retorno }
     */
    public Retorno createRetorno() {
        return new Retorno();
    }

    /**
     * Create an instance of {@link Personas }
     */
    public Personas createPersonas() {
        return new Personas();
    }

    /**
     * Create an instance of {@link Persona }
     */
    public Persona createPersona() {
        return new Persona();
    }

    /**
     * Create an instance of {@link Certificados }
     */
    public Certificados createCertificados() {
        return new Certificados();
    }

    /**
     * Create an instance of {@link Certificado }
     */
    public Certificado createCertificado() {
        return new Certificado();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DatosEspecificosItTxartela }{@code >}
     */
    @XmlElementDecl(namespace = "http://intermediacion.redsara.es/scsp/esquemas/datosespecificos", name = "DatosEspecificos")
    public JAXBElement<DatosEspecificosItTxartela> createDatosEspecificos(DatosEspecificosItTxartela value) {
        return new JAXBElement<>(_DatosEspecificos_QNAME, DatosEspecificosItTxartela.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Traza }{@code >}
     */
    @XmlElementDecl(namespace = "http://intermediacion.redsara.es/scsp/esquemas/datosespecificos", name = "DatosTraza")
    public JAXBElement<Traza> createDatosTraza(Traza value) {
        return new JAXBElement<>(_DatosTraza_QNAME, Traza.class, null, value);
    }

}

