package co.edu.udistrital.mdp.eventos.entities.paymententity;

import co.edu.udistrital.mdp.eventos.entities.BaseEntity;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public abstract class MethodOfPaymentEntity extends BaseEntity {
    @PodamExclude
    @OneToOne(mappedBy = "methodOfPayment")
    private PurchaseEntity purchase;

    String type;
}
