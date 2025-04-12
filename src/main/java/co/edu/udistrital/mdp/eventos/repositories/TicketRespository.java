package co.edu.udistrital.mdp.eventos.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.eventEntitys.TicketEntity;

public interface TicketRespository extends JpaRepository<TicketEntity, Long> {
    
}
