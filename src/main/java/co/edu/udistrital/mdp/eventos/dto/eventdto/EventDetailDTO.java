package co.edu.udistrital.mdp.eventos.dto.eventdto;



import java.util.Date;
import java.util.List;
import co.edu.udistrital.mdp.eventos.dto.userdto.OrganizerDTO;
import lombok.Data;

@Data
public class EventDetailDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Date date;
    private OrganizerDTO organizer;
    private LocationDTO location;
    private List<ResourceDTO> resources;
    private List<TicketDTO> tickets;
}