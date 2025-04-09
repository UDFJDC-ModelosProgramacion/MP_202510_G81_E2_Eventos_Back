package co.edu.udistrital.mdp.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.PromoEntity;

public interface PromoRepository extends JpaRepository<PromoEntity, Long> {
    
}
