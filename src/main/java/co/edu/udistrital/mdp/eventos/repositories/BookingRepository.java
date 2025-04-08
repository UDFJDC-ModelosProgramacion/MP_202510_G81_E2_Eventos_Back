package co.edu.udistrital.mdp.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.BookingEntity;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

}
