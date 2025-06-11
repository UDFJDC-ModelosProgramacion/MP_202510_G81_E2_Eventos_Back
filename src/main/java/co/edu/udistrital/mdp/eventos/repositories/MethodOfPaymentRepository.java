package co.edu.udistrital.mdp.eventos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.paymententity.MethodOfPaymentEntity;

public interface MethodOfPaymentRepository extends JpaRepository<MethodOfPaymentEntity, Long>{
    List<MethodOfPaymentEntity> findByAssistantId(Long assistantId);

}
