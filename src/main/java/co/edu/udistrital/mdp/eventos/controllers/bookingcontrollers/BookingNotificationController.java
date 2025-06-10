package co.edu.udistrital.mdp.eventos.controllers.bookingcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.NotificationEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.bookingentity.BookingNotificationService;

@RestController
@RequestMapping("/api/bookings/{bookingId}/notification")
public class BookingNotificationController {

    @Autowired
    private BookingNotificationService service;

    @PostMapping("/{notificationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationEntity addNotification(@PathVariable Long bookingId, @PathVariable Long notificationId) throws EntityNotFoundException {
        return service.addNotification(bookingId, notificationId);
    }

    @GetMapping
    public NotificationEntity getNotification(@PathVariable Long bookingId) throws EntityNotFoundException {
        return service.getNotification(bookingId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeNotification(@PathVariable Long bookingId) throws EntityNotFoundException {
        service.removeNotification(bookingId);
    }
}
