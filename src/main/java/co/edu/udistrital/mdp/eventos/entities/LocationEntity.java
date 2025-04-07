package co.edu.udistrital.mdp.eventos.entities;


import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class LocationEntity extends BaseEntity{
    String name;
    String location;
    String type;
    int capacity;
    
}
