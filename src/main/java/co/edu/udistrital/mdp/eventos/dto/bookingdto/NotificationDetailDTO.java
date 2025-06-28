package co.edu.udistrital.mdp.eventos.dto.bookingdto;

import co.edu.udistrital.mdp.eventos.dto.userdto.AssistantDTO;
import lombok.Data;

@Data
public class NotificationDetailDTO extends NotificationDTO {
    private PurchaseDTO purchase;
    private BookingDTO booking;
    private AssistantDTO assistant;
}