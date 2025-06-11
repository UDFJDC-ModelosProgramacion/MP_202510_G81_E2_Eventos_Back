package co.edu.udistrital.mdp.eventos.dto.paymentdto;

import java.util.Date;

import lombok.Data;

@Data
public class CreditCardDTO extends CardDTO{
    private Date expirationDate;
}
