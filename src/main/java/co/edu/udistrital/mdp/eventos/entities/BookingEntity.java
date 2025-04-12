package co.edu.udistrital.mdp.eventos.entities;

import co.edu.udistrital.mdp.eventos.entities.userEntitys.AssistantEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class BookingEntity extends BaseEntity {
    @PodamExclude
    @ManyToOne
    private AssistantEntity assistant;

    @PodamExclude
    @ManyToOne
    private EventEntity event;

    @PodamExclude
    @OneToOne
    private NotificationEntity notification;

    Integer remainingSeats;
}
