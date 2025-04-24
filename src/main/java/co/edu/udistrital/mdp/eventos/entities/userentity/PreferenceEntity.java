package co.edu.udistrital.mdp.eventos.entities.userentity;

/*
 * El paquete co.edu.udistrital.mdp.eventos.entities.userentity
 * contiene las entidades OrganizerEntity, UserEntity, Assistantentitys y PreferenceEntity
 */

import co.edu.udistrital.mdp.eventos.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class PreferenceEntity extends BaseEntity {
    @PodamExclude
    @ManyToOne
    private AssistantEntity assistant;

    String description;
}
