package co.edu.udistrital.mdp.eventos.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class BookingEntity extends BaseEntity {
    Integer remainingSeats;
}
