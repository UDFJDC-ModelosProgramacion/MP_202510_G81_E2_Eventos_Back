package co.edu.udistrital.mdp.eventos.entities.userEntitys;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.eventos.entities.BookingEntity;
import co.edu.udistrital.mdp.eventos.entities.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.NotificationEntity;
import co.edu.udistrital.mdp.eventos.entities.PurchaseEntity;
import jakarta.persistence.Entity;
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
    private List<PreferenceEntity> preferences = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "assistant")
    private List<PurchaseEntity> purchases = new ArrayList<>();

    @PodamExclude
    @ManyToOne
    private EventEntity event;
}
