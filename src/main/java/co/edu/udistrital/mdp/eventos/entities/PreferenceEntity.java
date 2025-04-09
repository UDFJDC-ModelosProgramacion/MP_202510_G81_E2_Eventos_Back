package co.edu.udistrital.mdp.eventos.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class PreferenceEntity extends BaseEntity {
    String description;
}
