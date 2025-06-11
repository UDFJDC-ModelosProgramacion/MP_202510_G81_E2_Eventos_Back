package co.edu.udistrital.mdp.eventos.dto.eventdto;

import lombok.Data;

@Data
public class TicketDTO {
    private Long id;
    private Integer price;
    private Integer remaining;
    private String classification;
    private Long eventId;
}
