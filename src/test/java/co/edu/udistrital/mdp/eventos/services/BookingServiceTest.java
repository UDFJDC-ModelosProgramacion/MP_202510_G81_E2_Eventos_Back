package co.edu.udistrital.mdp.eventos.services;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.services.bookingentity.BookingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Test
    public void createBookingCorrectTest() {
        BookingEntity booking = new BookingEntity();
        booking.setRemainingSeats(5);

        BookingEntity result = bookingService.createBooking(booking);
        Assertions.assertNotNull(result.getId());
    }

    @Test
    public void createBookingInvalidTest() {
        BookingEntity booking = new BookingEntity();
        booking.setRemainingSeats(-1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(booking);
        });
    }

    @Test
    public void updateBookingCorrectTest() {
        BookingEntity booking = new BookingEntity();
        booking.setRemainingSeats(10);
        booking = bookingService.createBooking(booking);

        booking.setRemainingSeats(20);
        BookingEntity updated = bookingService.updateBooking(booking.getId(), booking);
        Assertions.assertEquals(20, updated.getRemainingSeats());
    }

    @Test
    public void updateBookingInvalidTest() {
        BookingEntity booking = new BookingEntity();
        booking.setRemainingSeats(10);
        booking = bookingService.createBooking(booking);

        booking.setRemainingSeats(-10);

        BookingEntity finalBooking = booking; 
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bookingService.updateBooking(finalBooking.getId(), finalBooking);
        });
    }

    @Test
    public void getBookingCorrectTest() {
        BookingEntity booking = new BookingEntity();
        booking.setRemainingSeats(8);
        booking = bookingService.createBooking(booking);

        BookingEntity found = bookingService.getBooking(booking.getId());
        Assertions.assertNotNull(found);
    }

    @Test
    public void getBookingNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bookingService.getBooking(999L);
        });
    }

    @Test
    public void deleteBookingCorrectTest() {
        BookingEntity booking = new BookingEntity();
        booking.setRemainingSeats(6);
        booking = bookingService.createBooking(booking);
    
        Long bookingId = booking.getId(); 
    
        bookingService.deleteBooking(bookingId);
    
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bookingService.getBooking(bookingId);
        });
    }
    
    @Test
    public void deleteBookingNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bookingService.deleteBooking(999L);
        });
    }
}