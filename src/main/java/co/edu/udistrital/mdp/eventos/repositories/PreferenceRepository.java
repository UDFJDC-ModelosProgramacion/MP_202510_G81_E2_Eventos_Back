package co.edu.udistrital.mdp.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.userentity.PreferenceEntity;

public interface PreferenceRepository extends JpaRepository<PreferenceEntity, Long> {
    
}
