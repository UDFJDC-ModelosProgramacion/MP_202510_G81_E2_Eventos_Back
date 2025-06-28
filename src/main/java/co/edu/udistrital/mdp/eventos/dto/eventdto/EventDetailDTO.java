package co.edu.udistrital.mdp.eventos.dto.eventdto;

import java.util.List;
import co.edu.udistrital.mdp.eventos.dto.userdto.OrganizerDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EventDetailDTO extends EventDTO {
    private OrganizerDTO organizer;
    private LocationDTO location;
    private List<ResourceDTO> resources;
    private List<TicketDTO> tickets;
}
