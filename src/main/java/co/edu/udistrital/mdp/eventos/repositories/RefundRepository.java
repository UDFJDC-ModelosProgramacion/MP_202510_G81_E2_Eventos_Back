package co.edu.udistrital.mdp.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.bookingEntitys.RefundEntity;

public interface RefundRepository extends JpaRepository<RefundEntity, Long> {

}
