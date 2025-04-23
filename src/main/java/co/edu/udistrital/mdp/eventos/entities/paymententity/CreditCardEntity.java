package co.edu.udistrital.mdp.eventos.entities.paymententity;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity

public abstract  class CreditCardEntity extends CardEntity {
    Integer installments;
}
