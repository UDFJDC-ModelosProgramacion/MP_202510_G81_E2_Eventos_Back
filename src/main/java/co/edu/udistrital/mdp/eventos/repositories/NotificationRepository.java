package co.edu.udistrital.mdp.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.bookingEntitys.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

}
