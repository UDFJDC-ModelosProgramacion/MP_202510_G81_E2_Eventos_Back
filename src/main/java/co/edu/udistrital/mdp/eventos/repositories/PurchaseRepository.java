package co.edu.udistrital.mdp.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {
    
}
