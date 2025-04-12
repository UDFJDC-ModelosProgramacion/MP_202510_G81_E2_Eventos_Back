package co.edu.udistrital.mdp.eventos.entities.userEntitys;


import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.eventos.entities.eventEntitys.EventEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class OrganizerEntity extends UserEntity{
    @PodamExclude
    @OneToMany(mappedBy = "organizer")
    private List<EventEntity> events = new ArrayList<>();

    String address;
}
