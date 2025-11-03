
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
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://intermediacion.redsara.es/scsp/esquemas/ittaxtela/online/respuesta/datosespecificos}DatosTraza" minOccurs="0"/&gt;
 *         &lt;element ref="{http://intermediacion.redsara.es/scsp/esquemas/ittaxtela/online/respuesta/datosespecificos}EstadoResultado" minOccurs="0"/&gt;
 *         &lt;element ref="{http://intermediacion.redsara.es/scsp/esquemas/ittaxtela/online/respuesta/datosespecificos}Personas" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "datosTraza",
    "estadoResultado",
    "personas"
})
@XmlRootElement(name = "Retorno")
public class Retorno {

    @XmlElement(name = "DatosTraza")
    protected Traza datosTraza;
    @XmlElement(name = "EstadoResultado")
    protected EstadoResultado estadoResultado;
    @XmlElement(name = "Personas")
    protected Personas personas;

    /**
     * Obtiene el valor de la propiedad datosTraza.
     * 
     * @return
     *     possible object is
     *     {@link Traza }
     *     
     */
    public Traza getDatosTraza() {
        return datosTraza;
    }

    /**
     * Define el valor de la propiedad datosTraza.
     * 
     * @param value
     *     allowed object is
     *     {@link Traza }
     *     
     */
    public void setDatosTraza(Traza value) {
        this.datosTraza = value;
    }

    /**
     * Obtiene el valor de la propiedad estadoResultado.
     * 
     * @return
     *     possible object is
     *     {@link EstadoResultado }
     *     
     */
    public EstadoResultado getEstadoResultado() {
        return estadoResultado;
    }

    /**
     * Define el valor de la propiedad estadoResultado.
     * 
     * @param value
     *     allowed object is
     *     {@link EstadoResultado }
     *     
     */
    public void setEstadoResultado(EstadoResultado value) {
        this.estadoResultado = value;
    }

    /**
     * Obtiene el valor de la propiedad personas.
     * 
     * @return
     *     possible object is
     *     {@link Personas }
     *     
     */
    public Personas getPersonas() {
        return personas;
    }

    /**
     * Define el valor de la propiedad personas.
     * 
     * @param value
     *     allowed object is
     *     {@link Personas }
     *     
     */
    public void setPersonas(Personas value) {
        this.personas = value;
    }

}
