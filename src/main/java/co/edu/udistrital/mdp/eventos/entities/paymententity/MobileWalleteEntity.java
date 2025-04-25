package co.edu.udistrital.mdp.eventos.entities.paymententity;

import jakarta.persistence.DiscriminatorValue;

/*
 * El paquete co.edu.udistrital.mdp.eventos.entities.paymententity 
 * contiene las entidades MethodOfPaymentEntity <--- MobilleWalletEntity, CardEntity <--- CreditCardEntity, DebitCardEntity
 */

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity

@DiscriminatorValue("WALLET")
public class MobileWalleteEntity extends MethodOfPaymentEntity {
    Integer phoneAccount;
    String typeOfWallet;
    Integer otpCode;
    Integer identityDocument;
    String email;
}
