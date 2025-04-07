package co.edu.udistrital.mdp.eventos.entities;

import java.util.Date;


import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class EventEntity extends BaseEntity{
    String name;
    String description;
    String category;
    Date date;
    Date hour;
}
