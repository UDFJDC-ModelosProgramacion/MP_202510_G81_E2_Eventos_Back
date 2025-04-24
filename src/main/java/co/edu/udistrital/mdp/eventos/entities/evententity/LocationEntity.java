package co.edu.udistrital.mdp.eventos.entities.evententity;

/*
 * El paquete co.edu.udistrital.mdp.eventos.entities.evententity
 * contiene las entidades EventEntity, LocationEntity, ResourceEntity y TicketEntity
 */


import co.edu.udistrital.mdp.eventos.entities.BaseEntity;
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
    Integer capacity;
}
