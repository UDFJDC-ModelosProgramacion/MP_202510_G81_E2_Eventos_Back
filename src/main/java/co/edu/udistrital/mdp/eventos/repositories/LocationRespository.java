package co.edu.udistrital.mdp.eventos.repositories;
import co.edu.udistrital.mdp.eventos.entities.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRespository extends JpaRepository<LocationEntity, Long> {
    
}
