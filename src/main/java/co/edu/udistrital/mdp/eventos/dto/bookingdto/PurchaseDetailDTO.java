package co.edu.udistrital.mdp.eventos.dto.bookingdto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PurchaseDetailDTO extends PurchaseDTO {
    private List<RefundDTO> refunds = new ArrayList<>();
    private List<PromoDTO> promos = new ArrayList<>();
}
