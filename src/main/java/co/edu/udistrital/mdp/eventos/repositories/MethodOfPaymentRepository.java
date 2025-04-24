package co.edu.udistrital.mdp.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.paymententity.MethodOfPaymentEntity;

public interface MethodOfPaymentRepository extends JpaRepository<MethodOfPaymentEntity, Long> {

}