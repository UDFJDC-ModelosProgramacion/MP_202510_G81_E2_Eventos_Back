package co.edu.udistrital.mdp.eventos.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.eventEntitys.EventEntity;

public interface EventRespository extends JpaRepository<EventEntity, Long> {
    
}
