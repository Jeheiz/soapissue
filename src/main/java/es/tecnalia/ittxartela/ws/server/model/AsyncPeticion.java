package es.tecnalia.ittxartela.ws.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "async_peticion")
public class AsyncPeticion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_peticion", unique = true, nullable = false, length = 255)
    private String idPeticion;

    @Lob
    @Column(name = "xml_peticion", columnDefinition = "CLOB")
    private String xmlPeticion;

    @Lob
    @Column(name = "xml_respuesta", columnDefinition = "CLOB")
    private String xmlRespuesta;

    @Column(name = "estado", length = 10)
    private String estado;

    @Column(name = "ter")
    private Integer ter;
}