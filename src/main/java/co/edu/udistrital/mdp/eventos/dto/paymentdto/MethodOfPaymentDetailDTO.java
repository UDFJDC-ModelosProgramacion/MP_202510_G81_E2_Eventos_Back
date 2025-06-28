package co.edu.udistrital.mdp.eventos.dto.paymentdto;


import co.edu.udistrital.mdp.eventos.dto.userdto.AssistantDTO;
import lombok.Data;

@Data
public class MethodOfPaymentDetailDTO extends MethodOfPaymentDTO {
    private AssistantDTO assistant;
}
