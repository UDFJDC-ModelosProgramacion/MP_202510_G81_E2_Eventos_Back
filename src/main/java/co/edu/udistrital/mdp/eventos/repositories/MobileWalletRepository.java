package co.edu.udistrital.mdp.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.paymententity.MobileWalletEntity;

public interface MobileWalletRepository extends JpaRepository<MobileWalletEntity, Long>{

}
