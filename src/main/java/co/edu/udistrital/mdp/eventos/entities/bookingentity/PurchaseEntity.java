package co.edu.udistrital.mdp.eventos.entities.bookingentity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.eventos.entities.BaseEntity;
import co.edu.udistrital.mdp.eventos.entities.paymententity.MethodOfPaymentEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class PurchaseEntity extends BaseEntity {

    @PodamExclude
    @OneToMany(mappedBy = "purchase")
    private List<PromoEntity> promos = new ArrayList<>();

    @PodamExclude
    @ManyToOne
    private AssistantEntity assistant;

    @PodamExclude
    @OneToOne
    private NotificationEntity notification;

    @PodamExclude
    @OneToOne
    private RefundEntity refund;

    @PodamExclude
    @OneToOne
    private MethodOfPaymentEntity methodOfPayment;

    private Integer remainingSeats;

    private Double amount;

    private LocalDateTime purchaseDate;
}
