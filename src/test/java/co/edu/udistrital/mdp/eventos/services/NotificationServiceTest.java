package co.edu.udistrital.mdp.eventos.services.bookingentity;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.NotificationEntity;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Test
    public void createNotificationCorrectTest() {
        NotificationEntity notification = new NotificationEntity();
        notification.setDescription("Notification Test");

        NotificationEntity result = notificationService.createNotification(notification);
        Assertions.assertNotNull(result.getId());
    }

    @Test
    public void createNotificationInvalidTest() {
        NotificationEntity notification = new NotificationEntity();
        notification.setDescription("");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            notificationService.createNotification(notification);
        });
    }

    @Test
    public void updateNotificationCorrectTest() {
        NotificationEntity notification = new NotificationEntity();
        notification.setDescription("Original");
        notification = notificationService.createNotification(notification);

        notification.setDescription("Updated");
        NotificationEntity updated = notificationService.updateNotification(notification.getId(), notification);
        Assertions.assertEquals("Updated", updated.getDescription());
    }

    @Test
    public void updateNotificationInvalidTest() {
        NotificationEntity notification = new NotificationEntity();
        notification.setDescription("Valid");
        notification = notificationService.createNotification(notification);

        notification.setDescription("");

        NotificationEntity finalNotification = notification;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            notificationService.updateNotification(finalNotification.getId(), finalNotification);
        });
    }

    @Test
    public void getNotificationCorrectTest() {
        NotificationEntity notification = new NotificationEntity();
        notification.setDescription("Get Test");
        notification = notificationService.createNotification(notification);

        NotificationEntity found = notificationService.getNotification(notification.getId());
        Assertions.assertNotNull(found);
    }

    @Test
    public void getNotificationNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            notificationService.getNotification(999L);
        });
    }

    @Test
    public void deleteNotificationCorrectTest() {
        NotificationEntity notification = new NotificationEntity();
        notification.setDescription("Delete Test");
        notification = notificationService.createNotification(notification);

        Long notificationId = notification.getId();

        notificationService.deleteNotification(notificationId);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            notificationService.getNotification(notificationId);
        });
    }

    @Test
    public void deleteNotificationNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            notificationService.deleteNotification(999L);
        });
    }
}