package co.edu.udistrital.mdp.eventos.entities;



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

    int price;
    int remaining;
    String classification;
    
}
