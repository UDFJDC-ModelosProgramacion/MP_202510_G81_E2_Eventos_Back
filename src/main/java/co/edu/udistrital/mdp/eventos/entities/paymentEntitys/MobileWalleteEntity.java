package co.edu.udistrital.mdp.eventos.entities.paymentEntitys;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity

public abstract class MobileWalleteEntity extends MethodOfPaymentEntity {
    Integer phoneAccount;
    String typeOfWallet;
    Integer OTPCode;
    Integer identityDocument;
    String email;
}
