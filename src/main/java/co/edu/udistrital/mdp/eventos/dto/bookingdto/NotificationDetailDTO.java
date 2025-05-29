package co.edu.udistrital.mdp.eventos.dto.bookingdto;

import lombok.Data;

@Data
public class NotificationDetailDTO extends NotificationDTO {
    private PurchaseDTO purchase;
    private BookingDTO booking;
}