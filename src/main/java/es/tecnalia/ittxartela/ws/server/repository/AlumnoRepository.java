package es.tecnalia.ittxartela.ws.server.repository;

import es.tecnalia.ittxartela.ws.server.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoRepository extends JpaRepository<Alumno, String> {
    boolean existsByDni(String dni);
}
