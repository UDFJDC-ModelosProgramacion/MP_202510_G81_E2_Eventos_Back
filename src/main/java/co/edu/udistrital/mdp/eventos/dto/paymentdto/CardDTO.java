package co.edu.udistrital.mdp.eventos.dto.paymentdto;

import lombok.Data;

@Data
public class CardDTO extends MethodOfPaymentDTO{
    private Integer cardNumber;
    private String cardHolderName;
    private Integer securityCode;
    private Long assistantId; 
}
