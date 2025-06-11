package co.edu.udistrital.mdp.eventos.services;

import co.edu.udistrital.mdp.eventos.services.evententity.EventTicketService;



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.TicketEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import jakarta.transaction.Transactional;

import java.util.List;

@DataJpaTest
@Transactional
@Import(EventTicketService.class)
public class EventTicketServiceTest {

    @Autowired
    private EventTicketService eventTicketService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private EventEntity sampleEvent;
    private EventEntity secondEvent;
    private TicketEntity sampleTicket;
    private TicketEntity secondTicket;

    @BeforeEach
    void setUp() {
        sampleEvent = factory.manufacturePojo(EventEntity.class);
        entityManager.persist(sampleEvent);

        secondEvent = factory.manufacturePojo(EventEntity.class);
        entityManager.persist(secondEvent);

        sampleTicket = factory.manufacturePojo(TicketEntity.class);
        entityManager.persist(sampleTicket);

        secondTicket = factory.manufacturePojo(TicketEntity.class);
        entityManager.persist(secondTicket);
    }

    @Test
    void testAddTicketToEvent() throws EntityNotFoundException, IllegalOperationException {
        TicketEntity result = eventTicketService.addTicketToEvent(sampleEvent.getId(), sampleTicket.getId());
        
        assertNotNull(result);
        assertEquals(sampleTicket.getId(), result.getId());
        assertEquals(sampleEvent.getId(), result.getEvent().getId());
    }

    @Test
    void testAddTicketAlreadyAssignedToOtherEvent() throws EntityNotFoundException, IllegalOperationException {
        eventTicketService.addTicketToEvent(sampleEvent.getId(), sampleTicket.getId());
        
        assertThrows(IllegalOperationException.class, 
            () -> eventTicketService.addTicketToEvent(secondEvent.getId(), sampleTicket.getId()));
    }

    @Test
    void testGetEventTickets() throws EntityNotFoundException, IllegalOperationException {
        eventTicketService.addTicketToEvent(sampleEvent.getId(), sampleTicket.getId());
        
        List<TicketEntity> tickets = eventTicketService.getEventTickets(sampleEvent.getId());
        assertFalse(tickets.isEmpty());
        assertEquals(sampleTicket.getId(), tickets.get(0).getId());
    }

    @Test
    void testGetEventTicket() throws EntityNotFoundException, IllegalOperationException {
        eventTicketService.addTicketToEvent(sampleEvent.getId(), sampleTicket.getId());
        
        TicketEntity found = eventTicketService.getEventTicket(sampleEvent.getId(), sampleTicket.getId());
        assertEquals(sampleTicket.getId(), found.getId());
    }

    @Test
    void testGetNonAssociatedTicket() {
        assertThrows(IllegalOperationException.class, 
            () -> eventTicketService.getEventTicket(sampleEvent.getId(), sampleTicket.getId()));
    }

    @Test
    void testRemoveTicketFromEvent() throws EntityNotFoundException, IllegalOperationException {
        eventTicketService.addTicketToEvent(sampleEvent.getId(), sampleTicket.getId());
        
        eventTicketService.removeTicketFromEvent(sampleEvent.getId(), sampleTicket.getId());
        
        EventEntity updatedEvent = entityManager.find(EventEntity.class, sampleEvent.getId());
        assertTrue(updatedEvent.getTickets().isEmpty());
    }

    @Test
    void testReplaceEventTickets() throws EntityNotFoundException, IllegalOperationException  {
        eventTicketService.addTicketToEvent(sampleEvent.getId(), sampleTicket.getId());
        
        List<TicketEntity> newTickets = List.of(secondTicket);
        List<TicketEntity> result = eventTicketService.replaceEventTickets(sampleEvent.getId(), newTickets);
        
        assertEquals(1, result.size());
        assertEquals(secondTicket.getId(), result.get(0).getId());
        
        EventEntity updatedEvent = entityManager.find(EventEntity.class, sampleEvent.getId());
        assertEquals(1, updatedEvent.getTickets().size());
    }

    @Test
    void testCreateAndAddTicketsToEvent() throws EntityNotFoundException {
        TicketEntity newTicket = factory.manufacturePojo(TicketEntity.class);
        entityManager.persist(newTicket);
        
        List<TicketEntity> result = eventTicketService.createAndAddTicketsToEvent(
            sampleEvent.getId(), List.of(newTicket));
        
        assertEquals(1, result.size());
        assertEquals(newTicket.getId(), result.get(0).getId());
        
        EventEntity updatedEvent = entityManager.find(EventEntity.class, sampleEvent.getId());
        assertEquals(1, updatedEvent.getTickets().size());
    }

    @Test
    void testAddTicketToNonExistingEvent() {
        assertThrows(EntityNotFoundException.class, 
            () -> eventTicketService.addTicketToEvent(999L, sampleTicket.getId()));
    }

    @Test
    void testAddNonExistingTicketToEvent() {
        assertThrows(EntityNotFoundException.class, 
            () -> eventTicketService.addTicketToEvent(sampleEvent.getId(), 999L));
    }
}