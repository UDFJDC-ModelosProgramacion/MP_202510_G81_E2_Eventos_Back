package co.edu.udistrital.mdp.eventos.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data 

@Entity
public class AssistantEntity extends UserEntity{
    @PodamExclude

    @OneToMany(mappedBy = "booking")

    private List<BookingEntity> bookings = new ArrayList<>();

    @PodamExclude

    @OneToMany(mappedBy = "notification")

    private List<NotificationEntity> notifications = new ArrayList<>();
}
