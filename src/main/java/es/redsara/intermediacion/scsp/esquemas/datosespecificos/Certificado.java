package es.redsara.intermediacion.scsp.esquemas.datosespecificos;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "Certificado")
public class Certificado {

    @XmlElement(name = "CodigoModulo", required = true)
    protected String codigoModulo;
    @XmlElement(name = "FechaExamen")
    protected String fechaExamen;

    /**
     * Obtiene el valor de la propiedad codigoModulo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoModulo() {
        return codigoModulo;
    }

    /**
     * Define el valor de la propiedad codigoModulo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoModulo(String value) {
        this.codigoModulo = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaExamen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaExamen() {
        return fechaExamen;
    }

    /**
     * Define el valor de la propiedad fechaExamen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaExamen(String value) {
        this.fechaExamen = value;
    }

}
