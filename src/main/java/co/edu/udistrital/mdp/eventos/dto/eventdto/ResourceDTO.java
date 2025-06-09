package co.edu.udistrital.mdp.eventos.dto.eventdto;


import lombok.Data;

@Data
public class ResourceDTO {
    private Long id;
    private String url;
    private String type;
    private Long eventId; 
}