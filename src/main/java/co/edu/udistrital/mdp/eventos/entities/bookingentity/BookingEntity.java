package co.edu.udistrital.mdp.eventos.entities.bookingentity;

/*
 * El paquete co.edu.udistrital.mdp.eventos.entities.bookingentity 
 * contiene las entidades BookingEntity, NotificationEntity, PurchaseEntity, RefundEntity y PromoEntity
 */

import co.edu.udistrital.mdp.eventos.entities.BaseEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
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

    private Integer remainingSeats;
    
}
