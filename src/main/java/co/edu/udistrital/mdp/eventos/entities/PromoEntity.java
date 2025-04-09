package co.edu.udistrital.mdp.eventos.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class PromoEntity extends PurchaseEntity{
    Float discount;
    String description;
    String code;
}
