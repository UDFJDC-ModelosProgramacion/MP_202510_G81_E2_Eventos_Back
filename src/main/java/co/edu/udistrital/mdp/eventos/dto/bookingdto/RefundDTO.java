package co.edu.udistrital.mdp.eventos.dto.bookingdto;

import java.util.Date;
import lombok.Data;

@Data
public class RefundDTO {
    private Long id;
    private Date date;
    private String reason;

}
