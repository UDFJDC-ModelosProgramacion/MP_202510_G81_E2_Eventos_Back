package co.edu.udistrital.mdp.eventos.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class LocationEntity extends BaseEntity{
    @PodamExclude
    @OneToOne
    private EventEntity event;
    String name;
    String location;
    String type;
    int capacity;
    
}
