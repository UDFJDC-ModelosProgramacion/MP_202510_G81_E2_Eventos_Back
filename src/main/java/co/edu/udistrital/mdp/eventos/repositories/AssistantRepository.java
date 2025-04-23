package co.edu.udistrital.mdp.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.eventos.entities.userEntitys.Assistantentitys;

public interface AssistantRepository extends JpaRepository<Assistantentitys, Long>{
    
}
