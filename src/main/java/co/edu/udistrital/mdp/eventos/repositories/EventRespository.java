package co.edu.udistrital.mdp.eventos.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;

public interface EventRespository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByOrganizerId(Long organizerId);
}
