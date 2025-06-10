package co.edu.udistrital.mdp.eventos.services.bookingentity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.NotificationEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.repositories.BookingRepository;
import co.edu.udistrital.mdp.eventos.repositories.NotificationRepository;

@Service
public class BookingNotificationService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public NotificationEntity addNotification(Long bookingId, Long notificationId) throws EntityNotFoundException {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.BOOKING_NOT_FOUND));

        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND));

        booking.setNotification(notification);
        bookingRepository.save(booking);
        return notification;
    }

    @Transactional
    public NotificationEntity getNotification(Long bookingId) throws EntityNotFoundException {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.BOOKING_NOT_FOUND));

        if (booking.getNotification() == null)
            throw new EntityNotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND);

        return booking.getNotification();
    }

    @Transactional
    public void removeNotification(Long bookingId) throws EntityNotFoundException {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.BOOKING_NOT_FOUND));

        booking.setNotification(null);
        bookingRepository.save(booking);
    }
}
