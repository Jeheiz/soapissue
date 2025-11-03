
package es.redsara.intermediacion.scsp.esquemas.datosespecificos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="Documentacion" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element ref="{http://intermediacion.redsara.es/scsp/esquemas/ittaxtela/online/respuesta/datosespecificos}Certificados" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "Persona")
public class Persona {

    @XmlElement(name = "Documentacion", required = true)
    protected String documentacion;
    @XmlElement(name = "Certificados")
    protected Certificados certificados;

    /**
     * Obtiene el valor de la propiedad documentacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentacion() {
        return documentacion;
    }

    /**
     * Define el valor de la propiedad documentacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentacion(String value) {
        this.documentacion = value;
    }

    /**
     * Obtiene el valor de la propiedad certificados.
     * 
     * @return
     *     possible object is
     *     {@link Certificados }
     *     
     */
    public Certificados getCertificados() {
        return certificados;
    }

    /**
     * Define el valor de la propiedad certificados.
     * 
     * @param value
     *     allowed object is
     *     {@link Certificados }
     *     
     */
    public void setCertificados(Certificados value) {
        this.certificados = value;
    }

}
