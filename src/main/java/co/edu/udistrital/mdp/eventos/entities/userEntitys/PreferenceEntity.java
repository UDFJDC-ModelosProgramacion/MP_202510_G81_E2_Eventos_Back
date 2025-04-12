package co.edu.udistrital.mdp.eventos.entities.userEntitys;

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
