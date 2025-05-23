package co.edu.udistrital.mdp.eventos.entities.paymententity;

/*
 * El paquete co.edu.udistrital.mdp.eventos.entities.paymententity 
 * contiene las entidades MethodOfPaymentEntity <--- MobilleWalletEntity, CardEntity <--- CreditCardEntity, DebitCardEntity
 */

import java.util.Date;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity

@DiscriminatorValue("CARD")
public abstract class CardEntity extends MethodOfPaymentEntity {
    Integer cardNumber;
    String cardHolderName;
    Date expirationDate;
    Integer securityCode;
}
