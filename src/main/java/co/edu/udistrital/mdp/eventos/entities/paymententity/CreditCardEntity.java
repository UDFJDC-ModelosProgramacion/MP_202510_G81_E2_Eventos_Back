package co.edu.udistrital.mdp.eventos.entities.paymententity;

import java.util.Date;

import jakarta.persistence.DiscriminatorValue;

/*
 * El paquete co.edu.udistrital.mdp.eventos.entities.paymententity 
 * contiene las entidades MethodOfPaymentEntity <--- MobilleWalletEntity, CardEntity <--- CreditCardEntity, DebitCardEntity
 */

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity

@DiscriminatorValue("CREDIT_CARD")
public class CreditCardEntity extends CardEntity {
    Date expirationDate;
}
