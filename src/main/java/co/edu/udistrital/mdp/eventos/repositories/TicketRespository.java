package co.edu.udistrital.mdp.eventos.repositories;
import co.edu.udistrital.mdp.eventos.entities.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRespository extends JpaRepository<TicketEntity, Long> {
    
}
