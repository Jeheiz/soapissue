
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
 *         &lt;element re="{http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta}NombreCompletoFuncionario" minOccurs="0"/&gt;
 *         &lt;element re="{http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta}NifFuncionario" minOccurs="0"/&gt;
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
    "nombreCompletoFuncionario",
    "nifFuncionario"
})
@XmlRootElement(name = "Funcionario")
public class Funcionario {

    @XmlElement(name = "NombreCompletoFuncionario")
    protected String nombreCompletoFuncionario;
    @XmlElement(name = "NifFuncionario")
    protected String nifFuncionario;

    /**
     * Obtiene el valor de la propiedad nombreCompletoFuncionario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreCompletoFuncionario() {
        return nombreCompletoFuncionario;
    }

    /**
     * Define el valor de la propiedad nombreCompletoFuncionario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreCompletoFuncionario(String value) {
        this.nombreCompletoFuncionario = value;
    }

    /**
     * Obtiene el valor de la propiedad nifFuncionario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifFuncionario() {
        return nifFuncionario;
    }

    /**
     * Define el valor de la propiedad nifFuncionario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifFuncionario(String value) {
        this.nifFuncionario = value;
    }

}
