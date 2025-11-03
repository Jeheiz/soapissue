
package es.redsara.intermediacion.scsp.esquemas.datosespecificos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "certificado"
})
@XmlRootElement(name = "Certificados")
public class Certificados {

    @XmlElement(name = "Certificado")
    protected List<Certificado> certificado;

    public List<Certificado> getCertificado() {
        if (certificado == null) {
            certificado = new ArrayList<Certificado>();
        }
        return this.certificado;
    }

}
