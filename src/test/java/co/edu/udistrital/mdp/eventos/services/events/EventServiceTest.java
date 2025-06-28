package co.edu.udistrital.mdp.eventos.services.events;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.OrganizerEntity;
import co.edu.udistrital.mdp.eventos.services.evententity.EventService;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import jakarta.transaction.Transactional;

@DataJpaTest
@Transactional
@Import(EventService.class)
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private EventEntity sampleEvent;
    private OrganizerEntity sampleOrganizer;

    @BeforeEach
    void setUp() {
        sampleOrganizer = factory.manufacturePojo(OrganizerEntity.class);
        entityManager.persist(sampleOrganizer);

        sampleEvent = factory.manufacturePojo(EventEntity.class);
        sampleEvent.setOrganizer(sampleOrganizer);
        entityManager.persist(sampleEvent);
    }

    @Test
    void testCreateEvent() throws EntityNotFoundException {
        EventEntity newEvent = factory.manufacturePojo(EventEntity.class);
        newEvent.setOrganizer(sampleOrganizer);

        EventEntity result = eventService.createEvent(newEvent);
        assertNotNull(result.getId());
        assertEquals(newEvent.getName(), result.getName());
    }

    @Test
    void testCreateEventWithoutOrganizer() {
        EventEntity newEvent = factory.manufacturePojo(EventEntity.class);
        newEvent.setOrganizer(null); // Asegúrate que no tenga organizador
        
        assertThrows(IllegalArgumentException.class, () -> eventService.createEvent(newEvent));
    }

    @Test
    void testGetEventById() throws EntityNotFoundException {
        EventEntity found = eventService.getEventById(sampleEvent.getId());
        assertEquals(sampleEvent.getId(), found.getId());
    }

    @Test
    void testUpdateEvent() throws EntityNotFoundException {
        EventEntity updates = factory.manufacturePojo(EventEntity.class);
        EventEntity updated = eventService.updateEvent(sampleEvent.getId(), updates);

        assertEquals(sampleEvent.getId(), updated.getId());
        assertEquals(updates.getName(), updated.getName());
    }

    @Test
    void testDeleteEvent() throws EntityNotFoundException {
        eventService.deleteEvent(sampleEvent.getId());
        assertNull(entityManager.find(EventEntity.class, sampleEvent.getId()));
    }

    @Test
    void testGetEventsByOrganizer() {
        List<EventEntity> events = eventService.getEventsByOrganizer(sampleOrganizer.getId());
        assertFalse(events.isEmpty());
        assertEquals(sampleEvent.getId(), events.get(0).getId());
    }
}