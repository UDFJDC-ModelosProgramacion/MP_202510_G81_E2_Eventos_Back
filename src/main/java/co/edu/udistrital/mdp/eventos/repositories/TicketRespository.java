package co.edu.udistrital.mdp.eventos.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import co.edu.udistrital.mdp.eventos.entities.evententity.TicketEntity;

public interface TicketRespository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findByEventId(Long eventId);
    List<TicketEntity> findByClassificationContainingIgnoreCase(String classification);
}
