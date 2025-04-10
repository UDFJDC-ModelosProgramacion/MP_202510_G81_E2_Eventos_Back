package co.edu.udistrital.mdp.eventos.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public abstract class MethodOfPaymentEntity extends BaseEntity {
    String type;
}
