package co.edu.udistrital.mdp.eventos.entities.bookingEntitys;

import java.util.Date;

import co.edu.udistrital.mdp.eventos.entities.BaseEntity;
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
