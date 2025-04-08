package co.edu.udistrital.mdp.eventos.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class RefundEntity extends BaseEntity{
    Date date;
}
