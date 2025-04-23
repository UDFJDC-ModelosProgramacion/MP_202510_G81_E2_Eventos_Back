package co.edu.udistrital.mdp.eventos.entities.eventEntitys;

import java.util.ArrayList;
import java.util.Date;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import java.util.List;

import co.edu.udistrital.mdp.eventos.entities.BaseEntity;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.entities.userEntitys.AssistantEntity;
import co.edu.udistrital.mdp.eventos.entities.userEntitys.OrganizerEntity;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class EventEntity extends BaseEntity{
    @PodamExclude
    @OneToOne(mappedBy = "event")
    private LocationEntity location;

    //*Asociaciones OneToMany */
    @PodamExclude
    @OneToMany(mappedBy = "event")
    private List<ResourceEntity> resource = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "event")
    private List<TicketEntity> tickets = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "event")
    private List<AssistantEntity> assitants = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "event")
    private List<BookingEntity> bookings = new ArrayList<>();

    //*Asociaciones ManyToOne */
    @PodamExclude
    @ManyToOne
    private OrganizerEntity organizer; 

    String name;
    String description;
    String category;
    @Temporal(TemporalType.TIMESTAMP)
    Date date;
}
