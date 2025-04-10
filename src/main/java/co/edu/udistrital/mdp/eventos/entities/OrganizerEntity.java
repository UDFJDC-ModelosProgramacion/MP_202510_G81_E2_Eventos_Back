package co.edu.udistrital.mdp.eventos.entities;


import java.util.ArrayList;
import java.util.List;

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
