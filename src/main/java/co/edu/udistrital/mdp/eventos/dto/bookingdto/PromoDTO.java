package co.edu.udistrital.mdp.eventos.dto.bookingdto;
import lombok.Data;

@Data
public class PromoDTO {
    private Long id;
    private Float discount;
    private String description;
    private String code;
}
