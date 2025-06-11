package co.edu.udistrital.mdp.eventos.services;
import java.util.List;



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.TicketEntity;
import co.edu.udistrital.mdp.eventos.services.evententity.TicketService;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import jakarta.transaction.Transactional;

@DataJpaTest
@Transactional
@Import(TicketService.class)
public class TicketServiceTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private TicketEntity sampleTicket;
    private EventEntity sampleEvent;

    @BeforeEach
    void setUp() {
        sampleEvent = factory.manufacturePojo(EventEntity.class);
        entityManager.persist(sampleEvent);

        sampleTicket = factory.manufacturePojo(TicketEntity.class);
        sampleTicket.setEvent(sampleEvent);
        sampleTicket.setRemaining(10);
        entityManager.persist(sampleTicket);
    }

    @Test
    void testCreateTicket() throws EntityNotFoundException, IllegalArgumentException {
        TicketEntity newTicket = factory.manufacturePojo(TicketEntity.class);
        newTicket.setEvent(sampleEvent);
        newTicket.setPrice(50);
        newTicket.setRemaining(20);

        TicketEntity result = ticketService.createTicket(newTicket);
        assertNotNull(result.getId());
        assertEquals(50, result.getPrice());
    }

    @Test
    void testCreateTicketWithNegativePrice() {
        TicketEntity newTicket = factory.manufacturePojo(TicketEntity.class);
        newTicket.setPrice(-10);

        assertThrows(IllegalArgumentException.class, () -> ticketService.createTicket(newTicket));
    }

    @Test
    void testUpdateTicketAvailability() throws EntityNotFoundException, IllegalArgumentException {
        TicketEntity updated = ticketService.updateTicketAvailability(sampleTicket.getId(), -2);
        assertEquals(8, updated.getRemaining());
    }

    @Test
    void testUpdateTicketAvailabilityInsufficient() {
        assertThrows(IllegalArgumentException.class, 
            () -> ticketService.updateTicketAvailability(sampleTicket.getId(), -15));
    }

    @Test
    void testGetTicketsByEvent() {
        List<TicketEntity> tickets = ticketService.getTicketsByEvent(sampleEvent.getId());
        assertFalse(tickets.isEmpty());
        assertEquals(sampleTicket.getId(), tickets.get(0).getId());
    }

    @Test
    void testDeleteTicket() throws EntityNotFoundException {
        ticketService.deleteTicket(sampleTicket.getId());
        assertNull(entityManager.find(TicketEntity.class, sampleTicket.getId()));
    }
}