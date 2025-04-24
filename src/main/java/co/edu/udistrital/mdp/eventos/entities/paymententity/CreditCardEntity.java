package co.edu.udistrital.mdp.eventos.entities.paymententity;

/*
 * El paquete co.edu.udistrital.mdp.eventos.entities.paymententity 
 * contiene las entidades MethodOfPaymentEntity <--- MobilleWalletEntity, CardEntity <--- CreditCardEntity, DebitCardEntity
 */

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity

public abstract  class CreditCardEntity extends CardEntity {
    Integer installments;
}
