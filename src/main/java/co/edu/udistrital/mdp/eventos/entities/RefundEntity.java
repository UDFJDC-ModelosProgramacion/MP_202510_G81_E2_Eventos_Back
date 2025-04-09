package co.edu.udistrital.mdp.eventos.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class RefundEntity extends BaseEntity{
    @PodamExclude
    @OneToOne(mappedBy = "refund")
    private PurchaseEntity purchase;

    Date date;
}
