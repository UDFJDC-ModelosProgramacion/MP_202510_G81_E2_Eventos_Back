package co.edu.udistrital.mdp.eventos.services.bookingentity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.repositories.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Transactional
    public BookingEntity createBooking(BookingEntity booking) {
        // Puedes validar reglas de negocio aquí
        if (booking.getRemainingSeats() == null || booking.getRemainingSeats() < 0) {
            throw new IllegalArgumentException("Remaining seats must be non-negative");
        }
        return bookingRepository.save(booking);
    }

    public List<BookingEntity> getBookings() {
        return bookingRepository.findAll();
    }

    public BookingEntity getBooking(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Booking not found with id " + id));
    }

    @Transactional
    public BookingEntity updateBooking(Long id, BookingEntity booking) {
        BookingEntity existingBooking = getBooking(id);
        if (booking.getRemainingSeats() != null && booking.getRemainingSeats() < 0) {
            throw new IllegalArgumentException("Remaining seats must be non-negative");
        }
        existingBooking.setRemainingSeats(booking.getRemainingSeats());
        existingBooking.setAssistant(booking.getAssistant());
        existingBooking.setEvent(booking.getEvent());
        existingBooking.setNotification(booking.getNotification());
        return bookingRepository.save(existingBooking);
    }

    @Transactional
    public void deleteBooking(Long id) {
        BookingEntity booking = getBooking(id);
        bookingRepository.delete(booking);
    }
}