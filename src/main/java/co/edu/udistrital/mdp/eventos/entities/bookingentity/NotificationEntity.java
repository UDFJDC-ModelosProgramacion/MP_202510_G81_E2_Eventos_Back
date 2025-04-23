package co.edu.udistrital.mdp.eventos.entities.bookingentity;

import co.edu.udistrital.mdp.eventos.entities.BaseEntity;
import co.edu.udistrital.mdp.eventos.entities.userEntitys.Assistantentitys;
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
