package co.edu.udistrital.mdp.eventos.services;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.services.bookingentity.BookingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Test
    @DisplayName("Debe crear una reserva con datos válidos")
    public void createBookingValidTest() {
        BookingEntity booking = new BookingEntity();
        booking.setRemainingSeats(5);

        BookingEntity result = bookingService.createBooking(booking);

        assertNotNull(result.getId());
        assertEquals(5, result.getRemainingSeats());
    }

    @Test
    @DisplayName("No debe permitir crear una reserva con asientos negativos")
    public void createBookingInvalidTest() {
        BookingEntity booking = new BookingEntity();
        booking.setRemainingSeats(-1);

        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(booking));
    }

    @Test
    @DisplayName("Debe actualizar correctamente una reserva existente")
    public void updateBookingValidTest() {
        BookingEntity booking = new BookingEntity();
        booking.setRemainingSeats(10);
        booking = bookingService.createBooking(booking);

        booking.setRemainingSeats(20);
        BookingEntity updated = bookingService.updateBooking(booking.getId(), booking);

        assertEquals(20, updated.getRemainingSeats());
    }

    @Test
    @DisplayName("No debe permitir actualizar una reserva con asientos negativos")
    public void updateBookingInvalidTest() {
        BookingEntity booking = new BookingEntity();
        booking.setRemainingSeats(10);
        booking = bookingService.createBooking(booking);

        booking.setRemainingSeats(-5);

        BookingEntity finalBooking = booking;
        assertThrows(IllegalArgumentException.class, () -> bookingService.updateBooking(finalBooking.getId(), finalBooking));
    }

    @Test
    @DisplayName("Debe obtener correctamente una reserva existente")
    public void getBookingValidTest() {
        BookingEntity booking = new BookingEntity();
        booking.setRemainingSeats(8);
        booking = bookingService.createBooking(booking);

        BookingEntity found = bookingService.getBooking(booking.getId());

        assertNotNull(found);
        assertEquals(8, found.getRemainingSeats());
    }

    @Test
    @DisplayName("Debe lanzar excepción si la reserva no existe")
    public void getBookingNotFoundTest() {
        assertThrows(EntityNotFoundException.class, () -> bookingService.getBooking(999L));
    }

    @Test
    @DisplayName("Debe eliminar correctamente una reserva existente")
    public void deleteBookingValidTest() {
        BookingEntity booking = new BookingEntity();
        booking.setRemainingSeats(6);
        booking = bookingService.createBooking(booking);

        Long bookingId = booking.getId();
        bookingService.deleteBooking(bookingId);

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBooking(bookingId));
    }

    @Test
    @DisplayName("Debe lanzar excepción si se intenta eliminar una reserva inexistente")
    public void deleteBookingNotFoundTest() {
        assertThrows(EntityNotFoundException.class, () -> bookingService.deleteBooking(999L));
    }
}
