package co.edu.udistrital.mdp.eventos.services;


import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.userentity.booking.AssistantBookingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(AssistantBookingService.class)
public class AssistantBookingServiceTest {

    @Autowired
    private AssistantBookingService assistantBookingService;

    @Autowired
    private TestEntityManager entityManager;

    // Listas para almacenar la data insertada en las pruebas.
    private List<AssistantEntity> assistantData = new ArrayList<>();
    private List<BookingEntity> bookingData = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia la data de las tablas implicadas.
     */
    private void clearData() {
        entityManager.getEntityManager()
                    .createQuery("DELETE FROM BookingEntity")
                    .executeUpdate();
        entityManager.getEntityManager()
                    .createQuery("DELETE FROM AssistantEntity")
                    .executeUpdate();
    }

    /**
     * Inserta datos iniciales para las pruebas.
     */
    private void insertData() {
        // Creamos dos asistentes.
        AssistantEntity assistant1 = new AssistantEntity();
        assistant1.setName("Assistant 1");
        entityManager.persist(assistant1);
        assistantData.add(assistant1);

        AssistantEntity assistant2 = new AssistantEntity();
        assistant2.setName("Assistant 2");
        entityManager.persist(assistant2);
        assistantData.add(assistant2);

        // Creamos tres bookings.
        // Booking asociado a assistant1.
        BookingEntity booking1 = new BookingEntity();
        booking1.setRemainingSeats(10);
        booking1.setAssistant(assistant1);
        entityManager.persist(booking1);
        bookingData.add(booking1);
        assistant1.getBookings().add(booking1);

        // Booking sin asociación.
        BookingEntity booking2 = new BookingEntity();
        booking2.setRemainingSeats(5);
        // No se asocia aún a ningún assistant.
        entityManager.persist(booking2);
        bookingData.add(booking2);

        // Booking asociado a assistant2 (usado para verificar error de asociación).
        BookingEntity booking3 = new BookingEntity();
        booking3.setRemainingSeats(8);
        booking3.setAssistant(assistant2);
        entityManager.persist(booking3);
        bookingData.add(booking3);
        assistant2.getBookings().add(booking3);

        entityManager.flush();
    }

    // -------------------- TESTS PARA addBooking --------------------

    @Test
    public void testAddBookingValid() throws Exception {
        // Se asocia booking2 (sin relación) a assistant1.
        Long assistantId = assistantData.get(0).getId();
        Long bookingId = bookingData.get(1).getId();

        BookingEntity result = assistantBookingService.addBooking(assistantId, bookingId);
        assertNotNull(result);
        assertNotNull(result.getAssistant());
        assertEquals(assistantId, result.getAssistant().getId(), "El booking debe quedar asociado a assistant1");
    }

    @Test
    public void testAddBookingInvalidAssistant() {
        Long invalidAssistantId = 999L;
        Long bookingId = bookingData.get(1).getId();

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantBookingService.addBooking(invalidAssistantId, bookingId);
        });
        assertEquals(ErrorMessage.ASSISTANT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testAddBookingInvalidBooking() {
        Long assistantId = assistantData.get(0).getId();
        Long invalidBookingId = 999L;

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantBookingService.addBooking(assistantId, invalidBookingId);
        });
        assertEquals(ErrorMessage.BOOKING_NOT_FOUND, exception.getMessage());
    }

    // -------------------- TESTS PARA getBookings --------------------

    @Test
    public void testGetBookingsValid() throws Exception {
        // assistant1 tiene al menos un booking (booking1)
        Long assistantId = assistantData.get(0).getId();
        List<BookingEntity> bookings = assistantBookingService.getBookings(assistantId);
        assertNotNull(bookings);
        assertFalse(bookings.isEmpty(), "Debe devolver al menos un booking");
        // Verificar que cada booking esté asociado al mismo assistant.
        for (BookingEntity booking : bookings) {
            assertEquals(assistantId, booking.getAssistant().getId());
        }
    }

    @Test
    public void testGetBookingsInvalidAssistant() {
        Long invalidAssistantId = 999L;
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantBookingService.getBookings(invalidAssistantId);
        });
        assertEquals(ErrorMessage.ASSISTANT_NOT_FOUND, exception.getMessage());
    }

    // -------------------- TESTS PARA getBooking --------------------

    @Test
    public void testGetBookingValid() throws Exception {
        // Se obtiene booking1, el cual está asociado a assistant1.
        Long assistantId = assistantData.get(0).getId();
        Long bookingId = bookingData.get(0).getId();

        BookingEntity result = assistantBookingService.getBooking(assistantId, bookingId);
        assertNotNull(result);
        assertEquals(assistantId, result.getAssistant().getId());
    }

    @Test
    public void testGetBookingAssistantNotFound() {
        Long invalidAssistantId = 999L;
        Long bookingId = bookingData.get(0).getId();
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantBookingService.getBooking(invalidAssistantId, bookingId);
        });
        assertEquals(ErrorMessage.ASSISTANT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testGetBookingBookingNotFound() {
        Long assistantId = assistantData.get(0).getId();
        Long invalidBookingId = 999L;
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantBookingService.getBooking(assistantId, invalidBookingId);
        });
        assertEquals(ErrorMessage.BOOKING_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testGetBookingNotAssociated() {
        // booking3 está asociado a assistant2. Se intenta obtenerlo usando assistant1.
        Long assistant1Id = assistantData.get(0).getId();
        Long booking3Id = bookingData.get(2).getId();

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            assistantBookingService.getBooking(assistant1Id, booking3Id);
        });
        assertEquals("The booking is not associated to the assistant", exception.getMessage());
    }

    // -------------------- TESTS PARA removeBooking --------------------

    @Test
    public void testRemoveBookingValid() throws Exception {
        // Remover booking1 de assistant1.
        AssistantEntity assistant = assistantData.get(0);
        BookingEntity booking = bookingData.get(0);
        Long assistantId = assistant.getId();
        Long bookingId = booking.getId();

        // Verificar asociación inicial.
        assertEquals(assistantId, booking.getAssistant().getId());
        assertTrue(assistant.getBookings().contains(booking));

        // Removemos la asociación.
        assistantBookingService.removeBooking(assistantId, bookingId);

        // Se refrescan las entidades.
        BookingEntity removed = entityManager.find(BookingEntity.class, bookingId);
        AssistantEntity updatedAssistant = entityManager.find(AssistantEntity.class, assistantId);

        assertNull(removed.getAssistant(), "El booking ya no debe estar asociado a ningún assistant");
        assertFalse(updatedAssistant.getBookings().contains(removed), "El assistant ya no debe listar el booking removido");
    }

    @Test
    public void testRemoveBookingAssistantNotFound() {
        Long invalidAssistantId = 999L;
        Long bookingId = bookingData.get(0).getId();
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantBookingService.removeBooking(invalidAssistantId, bookingId);
        });
        assertEquals(ErrorMessage.ASSISTANT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testRemoveBookingBookingNotFound() {
        Long assistantId = assistantData.get(0).getId();
        Long invalidBookingId = 999L;
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantBookingService.removeBooking(assistantId, invalidBookingId);
        });
        assertEquals(ErrorMessage.BOOKING_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testRemoveBookingNotAssociated() {
        // Se intenta remover un booking que no está asociado al assistant1.
        // booking3 está asociado a assistant2.
        Long assistant1Id = assistantData.get(0).getId();
        Long booking3Id = bookingData.get(2).getId();
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantBookingService.removeBooking(assistant1Id, booking3Id);
        });
        assertEquals(ErrorMessage.BOOKING_NOT_ASSOCIATED, exception.getMessage());
    }
}
