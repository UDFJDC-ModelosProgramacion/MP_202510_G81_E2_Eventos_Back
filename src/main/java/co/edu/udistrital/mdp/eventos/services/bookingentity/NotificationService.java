package co.edu.udistrital.mdp.eventos.services.bookingentity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.NotificationEntity;
import co.edu.udistrital.mdp.eventos.repositories.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
     @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public NotificationEntity createNotification(NotificationEntity notification) {
        if (notification.getDescription() == null || notification.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        return notificationRepository.save(notification);
    }

    public List<NotificationEntity> getNotifications() {
        return notificationRepository.findAll();
    }

    public NotificationEntity getNotification(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id " + id));
    }

    @Transactional
    public NotificationEntity updateNotification(Long id, NotificationEntity notification) {
        NotificationEntity existing = getNotification(id);
        if (notification.getDescription() != null && notification.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        existing.setDescription(notification.getDescription());
        existing.setAssistant(notification.getAssistant());
        existing.setBooking(notification.getBooking());
        existing.setPurchase(notification.getPurchase());
        return notificationRepository.save(existing);
    }

    @Transactional
    public void deleteNotification(Long id) {
        NotificationEntity notification = getNotification(id);
        notificationRepository.delete(notification);
    }
}


