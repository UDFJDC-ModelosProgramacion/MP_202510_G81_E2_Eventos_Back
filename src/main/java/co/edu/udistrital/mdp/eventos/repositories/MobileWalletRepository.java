package co.edu.udistrital.mdp.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.paymentEntitys.MobileWalleteEntity;

public interface MobileWalletRepository extends JpaRepository<MobileWalleteEntity, Long>{

}
