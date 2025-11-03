
package es.redsara.intermediacion.scsp.esquemas.datosespecificos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Consulta", propOrder = {

})
   public class Consulta {
    @XmlElement(required = true)
    protected String idTraza;
    @XmlElement(name = "Documentacion", required = true)
    protected String documentacion;
    @XmlElement(name = "FechaLimite", required = true)
    protected String fechaLimite;
    @XmlElement(name = "TipoCertificacion")
    protected int tipoCertificacion;

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
     * Obtiene el valor de la propiedad fechaLimite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaLimite() {
        return fechaLimite;
    }

    /**
     * Define el valor de la propiedad fechaLimite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaLimite(String value) {
        this.fechaLimite = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoCertificacion.
     * 
     */
    public int getTipoCertificacion() {
        return tipoCertificacion;
    };

    /**
     * Define el valor de la propiedad tipoCertificacion.
     * 
     */
    public void setTipoCertificacion(int value) {
        this.tipoCertificacion = value;
    }

}
