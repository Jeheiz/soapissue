package es.tecnalia.ittxartela.ws.server.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "audit")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_peticion", nullable = false)
    private String idPeticion;

    @Lob
    @Column(name = "xml_peticion", columnDefinition = "CLOB")
    private String xmlPeticion;

    @Lob
    @Column(name = "xml_respuesta", columnDefinition = "CLOB")
    private String xmlRespuesta;

    @Column(name = "estado", nullable = false, length = 10)
    private String estado;

    @Column(name = "mensaje_estado", nullable = false, length = 255)
    private String mensajeEstado;

    @Column(name = "detalle_error", length = 500)
    private String detalleError;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(name = "fecha_respuesta_prevista", nullable = false)
    private LocalDateTime fechaRespuestaPrevista;

    @Column(name = "fecha_disponible")
    private LocalDateTime fechaDisponible;

    @PrePersist
    void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();
        this.fechaCreacion = ahora;
        this.fechaActualizacion = ahora;
    }

    @PreUpdate
    void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}