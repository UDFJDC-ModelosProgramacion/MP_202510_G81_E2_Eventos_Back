package co.edu.udistrital.mdp.eventos.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.evententity.ResourceEntity;

public interface ResourceRespository extends JpaRepository<ResourceEntity, Long> {
    
}
