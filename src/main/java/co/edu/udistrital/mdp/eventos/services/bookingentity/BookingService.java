package co.edu.udistrital.mdp.eventos.services.bookingentity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.repositories.BookingRepository;

import jakarta.transaction.Transactional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Transactional
    public BookingEntity createBooking(BookingEntity booking) {
        if (booking.getRemainingSeats() == null || booking.getRemainingSeats() < 0) {
            throw new IllegalArgumentException("Remaining seats must be non-negative");
        }
        return bookingRepository.save(booking);
    }

    public List<BookingEntity> getBookings() {
        return bookingRepository.findAll();
    }

    public BookingEntity getBooking(Long id) throws EntityNotFoundException {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id " + id));
    }

    @Transactional
    public BookingEntity updateBooking(Long id, BookingEntity booking) throws EntityNotFoundException {
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
    public void deleteBooking(Long id) throws EntityNotFoundException {
        BookingEntity booking = getBooking(id);
        bookingRepository.delete(booking);
    }
}
