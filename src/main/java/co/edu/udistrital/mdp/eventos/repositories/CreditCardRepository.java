package co.edu.udistrital.mdp.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.paymentEntitys.CreditCardEntity;

public interface CreditCardRepository extends JpaRepository<CreditCardEntity,Long>{

}
