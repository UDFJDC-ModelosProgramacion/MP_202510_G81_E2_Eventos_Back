package co.edu.udistrital.mdp.eventos.services;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.NotificationEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.repositories.BookingRepository;
import co.edu.udistrital.mdp.eventos.repositories.NotificationRepository;
import co.edu.udistrital.mdp.eventos.services.bookingentity.BookingNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BookingNotificationServiceTest {

    @Autowired
    private BookingNotificationService bookingNotificationService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private BookingEntity booking;
    private NotificationEntity notification;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        notificationRepository.deleteAll();

        booking = new BookingEntity();
        booking.setRemainingSeats(10);
        booking = bookingRepository.save(booking);

        notification = new NotificationEntity();
        notification.setDescription("Test Notification");
        notification = notificationRepository.save(notification);
    }

    @Test
    @DisplayName("Debe asociar una notificación a una reserva")
    void addNotificationTest() throws EntityNotFoundException {
        NotificationEntity result = bookingNotificationService.addNotification(booking.getId(), notification.getId());
        assertNotNull(result);
        assertEquals(notification.getId(), result.getId());
    }

    @Test
    @DisplayName("Debe obtener la notificación de una reserva")
    void getNotificationTest() throws EntityNotFoundException {
        bookingNotificationService.addNotification(booking.getId(), notification.getId());
        NotificationEntity found = bookingNotificationService.getNotification(booking.getId());
        assertNotNull(found);
    }

    @Test
    @DisplayName("Debe eliminar la notificación de una reserva")
    void removeNotificationTest() throws EntityNotFoundException {
        bookingNotificationService.addNotification(booking.getId(), notification.getId());
        bookingNotificationService.removeNotification(booking.getId());
        assertThrows(EntityNotFoundException.class, () -> bookingNotificationService.getNotification(booking.getId()));
    }
}