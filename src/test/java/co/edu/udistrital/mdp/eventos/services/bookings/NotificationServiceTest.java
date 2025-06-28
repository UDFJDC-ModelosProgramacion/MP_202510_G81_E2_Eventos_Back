package co.edu.udistrital.mdp.eventos.services.bookings;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.NotificationEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.repositories.NotificationRepository;
import co.edu.udistrital.mdp.eventos.services.bookingentity.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationServiceTest {

    @Autowired
    private NotificationService service;

    @Autowired
    private NotificationRepository repository;

    private NotificationEntity savedNotification;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        NotificationEntity notification = new NotificationEntity();
        notification.setDescription("Notificación inicial");
        savedNotification = service.createNotification(notification);
    }

    @Test
    @DisplayName("Debe crear una notificación con datos válidos")
    void createNotificationValidTest() {
        NotificationEntity notification = new NotificationEntity();
        notification.setDescription("Nueva notificación");
        NotificationEntity created = service.createNotification(notification);
        assertNotNull(created.getId());
        assertEquals("Nueva notificación", created.getDescription());
    }

    @Test
    @DisplayName("No debe permitir crear una notificación con descripción vacía")
    void createNotificationInvalidTest() {
        NotificationEntity notification = new NotificationEntity();
        notification.setDescription("");
        assertThrows(IllegalArgumentException.class, () ->
            service.createNotification(notification));
    }

    @Test
    @DisplayName("Debe obtener correctamente una notificación existente")
    void getNotificationValidTest() throws EntityNotFoundException {
        NotificationEntity found = service.getNotification(savedNotification.getId());
        assertEquals(savedNotification.getId(), found.getId());
        assertEquals(savedNotification.getDescription(), found.getDescription());
    }

    @Test
    @DisplayName("Debe lanzar excepción si la notificación no existe")
    void getNotificationNotFoundTest() {
        assertThrows(EntityNotFoundException.class, () ->
            service.getNotification(999L));
    }

    @Test
    @DisplayName("Debe actualizar correctamente una notificación existente")
    void updateNotificationValidTest() throws EntityNotFoundException {
        NotificationEntity updated = new NotificationEntity();
        updated.setDescription("Actualizada");
        NotificationEntity result = service.updateNotification(savedNotification.getId(), updated);
        assertEquals("Actualizada", result.getDescription());
    }

    @Test
    @DisplayName("No debe permitir actualizar una notificación con descripción vacía")
    void updateNotificationInvalidTest() {
        NotificationEntity updated = new NotificationEntity();
        updated.setDescription("");
        assertThrows(IllegalArgumentException.class, () ->
            service.updateNotification(savedNotification.getId(), updated));
    }

    @Test
    @DisplayName("Debe eliminar correctamente una notificación existente")
    void deleteNotificationValidTest() throws EntityNotFoundException {
        service.deleteNotification(savedNotification.getId());
        assertThrows(EntityNotFoundException.class, () ->
            service.getNotification(savedNotification.getId()));
    }

    @Test
    @DisplayName("Debe lanzar excepción si se intenta eliminar una notificación inexistente")
    void deleteNotificationNotFoundTest() {
        assertThrows(EntityNotFoundException.class, () ->
            service.deleteNotification(999L));
    }
}
