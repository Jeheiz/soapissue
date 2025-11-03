
package es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta;

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
&lt;element re="{http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta}CodProcedimiento" minOccurs="0"/&gt;
 *         &lt;element re="{http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta}NombreProcedimiento" minOccurs="0"/&gt;
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
    "codProcedimiento",
    "nombreProcedimiento"
})
@XmlRootElement(name = "Procedimiento")
public class Procedimiento {

    @XmlElement(name = "CodProcedimiento")
    protected String codProcedimiento;
    @XmlElement(name = "NombreProcedimiento")
    protected String nombreProcedimiento;

    /**
     * Obtiene el valor de la propiedad codProcedimiento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    /**
     * Define el valor de la propiedad codProcedimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodProcedimiento(String value) {
        this.codProcedimiento = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreProcedimiento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreProcedimiento() {
        return nombreProcedimiento;
    }

    /**
     * Define el valor de la propiedad nombreProcedimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreProcedimiento(String value) {
        this.nombreProcedimiento = value;
    }

}
