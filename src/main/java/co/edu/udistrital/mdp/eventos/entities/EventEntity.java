package co.edu.udistrital.mdp.eventos.entities;

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


import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class EventEntity extends BaseEntity{
    @PodamExclude
    @OneToOne(mappedBy = "event")
    private LocationEntity location;

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

    @PodamExclude
    @ManyToOne
    private OrganizerEntity organizer; 

    String name;
    String description;
    String category;
    @Temporal(TemporalType.TIMESTAMP)
    Date date;
}
