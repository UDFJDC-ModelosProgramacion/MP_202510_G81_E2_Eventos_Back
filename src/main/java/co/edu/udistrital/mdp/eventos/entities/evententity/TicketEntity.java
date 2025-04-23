package co.edu.udistrital.mdp.eventos.entities.evententity;



import co.edu.udistrital.mdp.eventos.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class TicketEntity extends BaseEntity{
    @PodamExclude
    @ManyToOne
    private EventEntity event;

    Integer price;
    Integer remaining;
    String classification;
}
