package co.edu.udistrital.mdp.eventos.entities.userentity;

/*
 * El paquete co.edu.udistrital.mdp.eventos.entities.userentity
 * contiene las entidades OrganizerEntity, UserEntity, Assistantentitys y PreferenceEntity
 */

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.NotificationEntity;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.paymententity.MethodOfPaymentEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data 
@Entity

public class AssistantEntity extends UserEntity{
    @PodamExclude
    @OneToMany(mappedBy = "assistant")
    private List<BookingEntity> bookings = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "assistant")
    private List<NotificationEntity> notifications = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "assistant")
    private List<PurchaseEntity> purchases = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "assistant")
    private List<MethodOfPaymentEntity> paymentMethods = new ArrayList<>();

    @PodamExclude
    @ManyToOne
    private EventEntity event;

    @PodamExclude
    @ManyToMany(mappedBy = "assistants")
    private List<PreferenceEntity> preferences = new ArrayList<>();
}
