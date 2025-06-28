package co.edu.udistrital.mdp.eventos.dto.eventdto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TicketDetailDTO extends TicketDTO {
    private EventDTO event;
}
