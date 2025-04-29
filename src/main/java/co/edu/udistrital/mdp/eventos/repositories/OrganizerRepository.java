package co.edu.udistrital.mdp.eventos.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.userentity.OrganizerEntity;

public interface OrganizerRepository extends JpaRepository<OrganizerEntity, Long>{
    Optional<OrganizerEntity> findByEmail(String email);

}
