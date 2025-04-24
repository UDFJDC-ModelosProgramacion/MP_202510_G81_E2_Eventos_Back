package co.edu.udistrital.mdp.eventos.entities.bookingentity;

/*
 * El paquete co.edu.udistrital.mdp.eventos.entities.bookingentity 
 * contiene las entidades BookingEntity, NotificationEntity, PurchaseEntity, RefundEntity y PromoEntity
 */

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class PromoEntity extends PurchaseEntity{
    @PodamExclude
    @ManyToOne
    private PurchaseEntity purchase;

    Float discount;
    String description;
    String code;
}
