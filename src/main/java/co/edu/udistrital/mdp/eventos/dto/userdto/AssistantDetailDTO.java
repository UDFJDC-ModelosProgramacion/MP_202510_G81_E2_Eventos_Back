package co.edu.udistrital.mdp.eventos.dto.userdto;

import java.util.ArrayList;
import java.util.List;


import co.edu.udistrital.mdp.eventos.dto.bookingdto.BookingDTO;
import co.edu.udistrital.mdp.eventos.dto.bookingdto.NotificationDTO;
import co.edu.udistrital.mdp.eventos.dto.bookingdto.PurchaseDTO;
import co.edu.udistrital.mdp.eventos.dto.paymentdto.MethodOfPaymentDTO;
import lombok.Data;

@Data
public class AssistantDetailDTO extends AssistantDTO{
    private List<PreferenceDTO> preferences = new ArrayList<>();
    private List<NotificationDTO> notifications = new ArrayList<>();
    private List<BookingDTO> bookings = new ArrayList<>();
    private List<PurchaseDTO> purchases = new ArrayList<>();
    private List<MethodOfPaymentDTO> methodOfPayments = new ArrayList<>();
}
