package co.edu.udistrital.mdp.eventos.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import co.edu.udistrital.mdp.eventos.entities.evententity.ResourceEntity;

public interface ResourceRespository extends JpaRepository<ResourceEntity, Long> {
    List<ResourceEntity> findByEventId(Long eventId);
    List<ResourceEntity> findByTypeContainingIgnoreCase(String type);
}
