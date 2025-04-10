package co.edu.udistrital.mdp.eventos.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public abstract  class CreditCardEntity extends CardEntity {
    Integer installments;
}
