package co.edu.udistrital.mdp.eventos.entities;



import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class ResourceEntity extends BaseEntity{
    @PodamExclude
    @ManyToOne
    private EventEntity event;
    
    String url;
    String type;
    
}
