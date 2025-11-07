package es.tecnalia.ittxartela.ws.server.model;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Getter
@Entity
@Table(name = "matricula")
public class Matricula {

    @Id
    private String password;

    @Column
    private Integer cod_centro;

    @Column
    private String dni;

    @Column
    private Integer cod_modulo;

    @Column
    private String username;

    @Column
    private Integer num_conv;

    @Column
    private Date fecha_matr;

    @Column
    private String num_envios_metaposta;

    @Column
    private Date fecha_exam;

    @Column
    private String hora_exam;

    @Column
    private String intentos;

    @Column
    private String ip;

    @Column
    private Integer id_status;

    @Column
    private Integer prueba_cerrada;

    @Column
    private Integer id_nivel;

    @Column
    private Integer num_correctas;

    @Column
    private Integer num_incorrectas;

    @Column
    private Integer acg1;

    @Column
    private Integer acg2;

    @Column
    private Integer acg3;

    @Column
    private Integer comunicacion_alternativo;

    @Column
    private Integer asistencia_especial;

    @Column
    private String useragent;

    @Column
    private String hora_inicio_examen;

    @Column
    private String hora_fin_examen;

    @Column
    private Integer examen_invidentes;

    @Column
    private Integer activo;

    @Column
    private Integer id_origen;

    @Column
    private String idSesion;

    @Column
    private Integer plataforma;

    @Column
    private Integer viejaConvocatoria;

    @Column
    private String resp1;

    @Column
    private String resp2;

    @Column
    private String resp3;

    @Column
    private Integer tiempo;

    @Column
    private Timestamp created;

    @Column
    private Timestamp updated;

    @Column
    private String idioma;
}

