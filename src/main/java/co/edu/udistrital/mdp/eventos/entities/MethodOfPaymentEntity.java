package co.edu.udistrital.mdp.eventos.entities;

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
