package es.tecnalia.ittxartela.ws.server.async;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Estado;

@XmlRootElement(name = "Acknowledge")
@XmlAccessorType(XmlAccessType.FIELD)
public class Acknowledge {

    @XmlElement(name = "IdPeticion")
    private String idPeticion;

    @XmlElement(name = "Estado")
    private Estado estado;

    public String getIdPeticion() {
        return idPeticion;
    }

    public void setIdPeticion(String idPeticion) {
        this.idPeticion = idPeticion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}