package co.edu.udistrital.mdp.eventos.repositories;
import co.edu.udistrital.mdp.eventos.entities.ResourceEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRespository extends JpaRepository<ResourceEntity, Long> {
    
}
