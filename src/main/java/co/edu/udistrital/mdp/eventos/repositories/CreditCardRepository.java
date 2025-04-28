package co.edu.udistrital.mdp.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.paymententity.CreditCardEntity;

public interface CreditCardRepository extends JpaRepository<CreditCardEntity,Long>{
    boolean existsByCardNumber(Integer cardNumber);
}
