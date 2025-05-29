package co.edu.udistrital.mdp.eventos.dto.bookingdto;

import lombok.Data;

@Data
public class PromoDetailDTO extends PromoDTO {
    private PurchaseDTO purchase;  // Promo asociada a una compra
}
