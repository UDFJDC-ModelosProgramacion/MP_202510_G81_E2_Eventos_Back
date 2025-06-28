package co.edu.udistrital.mdp.eventos.dto.bookingdto;

import co.edu.udistrital.mdp.eventos.dto.userdto.AssistantDTO;
import lombok.Data;

@Data
public class BookingDetailDTO extends BookingDTO {
    private AssistantDTO assistant;
}
