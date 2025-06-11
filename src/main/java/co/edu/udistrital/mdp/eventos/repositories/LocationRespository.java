package co.edu.udistrital.mdp.eventos.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import co.edu.udistrital.mdp.eventos.entities.evententity.LocationEntity;

public interface LocationRespository extends JpaRepository<LocationEntity, Long> {
    List<LocationEntity> findByEventId(Long eventId);
    List<LocationEntity> findByTypeContainingIgnoreCase(String type);
}
