package co.edu.udistrital.mdp.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.paymententity.DebitCardEntity;

public interface DebitCardRepository extends JpaRepository<DebitCardEntity, Long> {
    boolean existsByCardNumber(Integer cardNumber);
}
