package es.tecnalia.ittxartela.ws.server.model;

import java.sql.Timestamp;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "alumnos")
public class Alumno {

    @Id
    private String dni;

    @Column
    private String nombre;

    @Column
    private String apellido1;

    @Column
    private String apellido2;

    @Column
    private String direccion;

    @Column
    private String codigop;

    @Column
    private String poblacion;

    @Column
    private String provincia;

    @Column
    private String telefono;

    @Column
    private String email;

    @Column
    private Date edad;

    @Column
    private String sexo;

    @Column
    private String ocupacion;

    @Column
    private Integer anosti;

    @Column
    private String displayname;

    @Column
    private Integer maneraform;

    @Column
    private String perpassword;

    @Column
    private Integer id_provincias;

    @Column
    private Integer idsexo;

    @Column
    private Integer idsector;

    @Column
    private Integer idPerfil;

    @Column
    private String claveEmpresa;

    @Column
    private Integer id_origen;

    @Column
    private String preguntasecreta;

    @Column
    private String respuestasecreta;

    @Column
    private String erakundeIVAP;

    @Column
    private Timestamp created;

    @Column
    private Timestamp updated;

    @Column
    private Integer agreeGDPR;

    @Column
    private Integer activo;

    @Column
    private Timestamp fecha_confirmacion;
}
