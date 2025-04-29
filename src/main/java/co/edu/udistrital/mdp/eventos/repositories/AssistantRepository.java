package co.edu.udistrital.mdp.eventos.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;

public interface AssistantRepository extends JpaRepository<AssistantEntity, Long>{
    Optional<AssistantEntity> findByEmail(String email);

}
