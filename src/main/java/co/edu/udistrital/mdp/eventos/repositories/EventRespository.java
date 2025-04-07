package co.edu.udistrital.mdp.eventos.repositories;
import co.edu.udistrital.mdp.eventos.entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRespository extends JpaRepository<EventEntity, Long> {
    
}
