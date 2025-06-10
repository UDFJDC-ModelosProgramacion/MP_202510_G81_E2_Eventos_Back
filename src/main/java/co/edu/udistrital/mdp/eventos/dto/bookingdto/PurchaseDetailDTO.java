package co.edu.udistrital.mdp.eventos.dto.bookingdto;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.eventos.dto.userdto.AssistantDTO;
import lombok.Data;

@Data
public class PurchaseDetailDTO extends PurchaseDTO {
    private AssistantDTO assistant;
    private List<RefundDTO> refunds = new ArrayList<>();
    private List<PromoDTO> promos = new ArrayList<>();
}
