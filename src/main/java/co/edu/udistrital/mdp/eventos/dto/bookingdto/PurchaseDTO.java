package co.edu.udistrital.mdp.eventos.dto.bookingdto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PurchaseDTO {
    private Long id;
    private Integer remainingSeats;
    private Double amount;
    private LocalDateTime purchaseDate;
}
