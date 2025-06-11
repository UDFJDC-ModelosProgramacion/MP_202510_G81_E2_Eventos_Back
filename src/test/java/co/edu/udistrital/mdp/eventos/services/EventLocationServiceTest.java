package co.edu.udistrital.mdp.eventos.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.LocationEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.evententity.EventLocationService;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import jakarta.transaction.Transactional;

@DataJpaTest
@Transactional
@Import(EventLocationService.class)
public class EventLocationServiceTest {

    @Autowired
    private EventLocationService eventLocationService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private EventEntity sampleEvent;
    private LocationEntity sampleLocation;
    private LocationEntity secondLocation;

    @BeforeEach
    void setUp() {
        sampleEvent = factory.manufacturePojo(EventEntity.class);
        entityManager.persist(sampleEvent);

        sampleLocation = factory.manufacturePojo(LocationEntity.class);
        entityManager.persist(sampleLocation);

        secondLocation = factory.manufacturePojo(LocationEntity.class);
        entityManager.persist(secondLocation);
    }

    @Test
    void testAssignLocationToEvent() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity result = eventLocationService.assignLocationToEvent(sampleEvent.getId(), sampleLocation.getId());
        
        assertNotNull(result);
        assertEquals(sampleLocation.getId(), result.getId());
        assertEquals(sampleEvent.getId(), result.getEvent().getId());
    }

    @Test
    void testAssignLocationToEventAlreadyAssigned() throws EntityNotFoundException, IllegalOperationException {
        eventLocationService.assignLocationToEvent(sampleEvent.getId(), sampleLocation.getId());
        
        assertThrows(IllegalOperationException.class, 
            () -> eventLocationService.assignLocationToEvent(sampleEvent.getId(), secondLocation.getId()));
    }

    @Test
    void testGetEventLocation() throws EntityNotFoundException, IllegalOperationException {
        eventLocationService.assignLocationToEvent(sampleEvent.getId(), sampleLocation.getId());
        
        LocationEntity found = eventLocationService.getEventLocation(sampleEvent.getId());
        assertEquals(sampleLocation.getId(), found.getId());
    }

    @Test
    void testGetEventLocationWithoutLocation() {
        assertThrows(EntityNotFoundException.class, 
            () -> eventLocationService.getEventLocation(sampleEvent.getId()));
    }

    @Test
    void testRemoveLocationFromEvent() throws EntityNotFoundException, IllegalOperationException {
        eventLocationService.assignLocationToEvent(sampleEvent.getId(), sampleLocation.getId());
        
        eventLocationService.removeLocationFromEvent(sampleEvent.getId());
        
        EventEntity updatedEvent = entityManager.find(EventEntity.class, sampleEvent.getId());
        assertNull(updatedEvent.getLocation());
    }

    @Test
    void testUpdateEventLocation() throws EntityNotFoundException, IllegalOperationException {
        eventLocationService.assignLocationToEvent(sampleEvent.getId(), sampleLocation.getId());
        
        LocationEntity updated = eventLocationService.updateEventLocation(sampleEvent.getId(), secondLocation.getId());
        
        assertEquals(secondLocation.getId(), updated.getId());
        assertEquals(sampleEvent.getId(), updated.getEvent().getId());
        
        LocationEntity oldLocation = entityManager.find(LocationEntity.class, sampleLocation.getId());
        assertNull(oldLocation.getEvent());
    }

    @Test
    void testAssignLocationToNonExistingEvent() {
        assertThrows(EntityNotFoundException.class, 
            () -> eventLocationService.assignLocationToEvent(999L, sampleLocation.getId()));
    }

    @Test
    void testAssignNonExistingLocationToEvent() {
        assertThrows(EntityNotFoundException.class, 
            () -> eventLocationService.assignLocationToEvent(sampleEvent.getId(), 999L));
    }
}