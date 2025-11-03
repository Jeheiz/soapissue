package es.tecnalia.ittxartela.ws.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.tecnalia.ittxartela.ws.server.model.Audit;

public interface AuditRepository extends JpaRepository<Audit, Long> {
    Audit findTopByIdPeticionOrderByIdDesc(String idPeticion);

    List<Audit> findByEstadoOrderByFechaCreacionAsc(String estado);
}