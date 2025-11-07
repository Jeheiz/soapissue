package es.tecnalia.ittxartela.ws.server.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.tecnalia.ittxartela.ws.server.model.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, String> {

    @Query("SELECT m FROM Matricula m " +
           "WHERE UPPER(m.dni) = UPPER(:dni) " +
           "AND m.id_status = 1 " +
           "AND (m.plataforma IN (0, 1, 2) OR m.plataforma IS NULL) " +
           "AND (:tipoCertificacion IS NULL OR m.id_nivel = :tipoCertificacion) " +
           "AND m.fecha_exam <= :fechaLimite")
    List<Matricula> findCertificacionesValidas(
            @Param("dni") String dni,
            @Param("tipoCertificacion") Integer tipoCertificacion,
            @Param("fechaLimite") Date fechaLimite
    );

    default List<Matricula> obtenerCertificacionesAprobadas(String dni) {
        return obtenerCertificacionesAprobadas(dni, null, new Date(System.currentTimeMillis()));
    }

    default List<Matricula> obtenerCertificacionesAprobadas(String dni, Date fechaLimite) {
        return obtenerCertificacionesAprobadas(dni, null, fechaLimite);
    }

    default List<Matricula> obtenerCertificacionesAprobadas(String dni, Integer tipoCertificacion, Date fechaLimite) {
        return findCertificacionesValidas(dni, tipoCertificacion, fechaLimite);
    }
}

default List<Matricula> obtenerCertificacionesAprobadas(String dni, Integer tipoCertificacion, Date fechaLimite) {
 return findCertificacionesValidas(dni, tipoCertificacion, fechaLimite);
}
}
