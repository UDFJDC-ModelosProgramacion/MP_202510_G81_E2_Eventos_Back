package co.edu.udistrital.mdp.eventos.dto.eventdto;



import lombok.Data;

@Data
public class LocationDTO {
    private Long id;
    private String name;
    private String location; 
    private String type;
    private Integer capacity;
    private Long eventId; 
}