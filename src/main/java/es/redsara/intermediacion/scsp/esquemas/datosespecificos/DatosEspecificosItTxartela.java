package es.redsara.intermediacion.scsp.esquemas.datosespecificos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;




@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatosEspecificosItTxartela", propOrder = {

})
@XmlRootElement(name = "DatosEspecificos")
public class DatosEspecificosItTxartela {

    @XmlElement(name = "Consulta")
    protected Consulta consulta;
    @XmlElement(name = "Retorno")
    protected Retorno retorno;

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta value) {
        this.consulta = value;
    }

    /**
     * Obtiene el valor de la propiedad retorno.
     * 
     * @return
     *     possible object is
     *     {@link Retorno }
     *     
     */
    public Retorno getRetorno() {
        return retorno;
    }

    /**
     * Define el valor de la propiedad retorno.
     * 
     * @param value
     *     allowed object is
     *     {@link Retorno }
     *     
     */
    public void setRetorno(Retorno value) {
        this.retorno = value;
    }

}