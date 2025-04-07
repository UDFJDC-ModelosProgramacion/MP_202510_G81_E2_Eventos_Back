package co.edu.udistrital.mdp.eventos.entities;



import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class TicketEntity extends BaseEntity{
    int price;
    int remaining;
    String classification;
    
}
