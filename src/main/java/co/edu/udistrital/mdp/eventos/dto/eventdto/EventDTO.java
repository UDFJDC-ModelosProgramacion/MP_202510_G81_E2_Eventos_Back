package co.edu.udistrital.mdp.eventos.dto.eventdto;

import java.util.Date;
import lombok.Data;

@Data
public class EventDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Date date;
    private Long organizerId;
}
