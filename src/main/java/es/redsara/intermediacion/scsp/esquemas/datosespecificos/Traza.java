
package es.redsara.intermediacion.scsp.esquemas.datosespecificos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Traza complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Traza"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="IdTraza" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="IdPeticion" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Traza", propOrder = {

})
public class Traza {

    @XmlElement(name = "IdTraza", required = true)
    protected String idTraza;
    @XmlElement(name = "IdPeticion", required = true)
    protected String idPeticion;

    /**
     * Obtiene el valor de la propiedad idTraza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTraza() {
        return idTraza;
    }

    /**
     * Define el valor de la propiedad idTraza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTraza(String value) {
        this.idTraza = value;
    }

    /**
     * Obtiene el valor de la propiedad idPeticion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdPeticion() {
        return idPeticion;
    }

    /**
     * Define el valor de la propiedad idPeticion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdPeticion(String value) {
        this.idPeticion = value;
    }

}
