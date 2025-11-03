package es.tecnalia.ittxartela.ws.server.async;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Solicitud")
@XmlAccessorType(XmlAccessType.FIELD)
public class IdPeticion {

    @XmlElement(name = "IdPeticion")
    private String idPeticion;

    public String getIdPeticion() {
        return idPeticion;
    }

    public void setIdPeticion(String idPeticion) {
        this.idPeticion = idPeticion;
    }
}