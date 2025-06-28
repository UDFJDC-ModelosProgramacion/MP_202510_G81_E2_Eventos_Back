package co.edu.udistrital.mdp.eventos.dto.userdto;

import java.util.ArrayList;
import java.util.List;


import co.edu.udistrital.mdp.eventos.dto.eventdto.EventDTO;
import lombok.Data;

@Data
public class OrganizerDetailDTO extends OrganizerDTO{
    private List<EventDTO> events = new ArrayList<>();
}
