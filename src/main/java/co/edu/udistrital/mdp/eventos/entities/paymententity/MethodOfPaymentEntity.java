package co.edu.udistrital.mdp.eventos.entities.paymententity;

/*
 * El paquete co.edu.udistrital.mdp.eventos.entities.paymententity 
 * contiene las entidades MethodOfPaymentEntity <--- MobilleWalletEntity, CardEntity <--- CreditCardEntity, DebitCardEntity
 */

import co.edu.udistrital.mdp.eventos.entities.BaseEntity;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.DiscriminatorType;

import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

@Table(name = "method_of_payment")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "method_type", discriminatorType = DiscriminatorType.STRING)

public abstract class MethodOfPaymentEntity extends BaseEntity {
    @PodamExclude
    @OneToOne(mappedBy = "methodOfPayment")
    private PurchaseEntity purchase;

    @PodamExclude
    @ManyToOne
    private AssistantEntity assistant;

    @Column(nullable = false)
    String type;
}
