package co.edu.udistrital.mdp.eventos.dto.bookingdto;

import lombok.Data;

@Data
public class RefundDetailDTO extends RefundDTO {
    private PurchaseDTO purchase;  // Reembolso asociado a una compra
}