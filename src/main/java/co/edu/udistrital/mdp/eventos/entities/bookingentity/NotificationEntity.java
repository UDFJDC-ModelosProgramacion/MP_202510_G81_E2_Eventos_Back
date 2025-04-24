package co.edu.udistrital.mdp.eventos.entities.bookingentity;

/*
 * El paquete co.edu.udistrital.mdp.eventos.entities.bookingentity 
 * contiene las entidades BookingEntity, NotificationEntity, PurchaseEntity, RefundEntity
 */

import co.edu.udistrital.mdp.eventos.entities.BaseEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.Assistantentitys;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class NotificationEntity extends BaseEntity {
    @PodamExclude
    @OneToOne(mappedBy = "notification")
    private BookingEntity booking;

    @PodamExclude
    @OneToOne(mappedBy = "notification")
    private PurchaseEntity purchase;

    @PodamExclude
    @ManyToOne
    private Assistantentitys assistant;
    
    String description;
}
