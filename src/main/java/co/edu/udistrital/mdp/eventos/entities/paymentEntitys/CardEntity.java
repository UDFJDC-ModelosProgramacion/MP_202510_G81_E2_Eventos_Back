package co.edu.udistrital.mdp.eventos.entities.paymentEntitys;

import java.util.Date;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity

public abstract class CardEntity extends MethodOfPaymentEntity {
    Integer cardNumber;
    String cardHolderName;
    Date expirationDate;
    Integer securityCode;
}
