package co.edu.udistrital.mdp.eventos.dto.paymentdto;

import lombok.Data;

@Data
public class MobileWalletDTO extends MethodOfPaymentDTO {

    Integer phoneAccount;
    String typeOfWallet;
    Integer OTPCode;
    Integer identityDocument;
    String email;
}
