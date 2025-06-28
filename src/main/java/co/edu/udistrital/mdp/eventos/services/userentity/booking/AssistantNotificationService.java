package co.edu.udistrital.mdp.eventos.services.userentity.booking;


import co.edu.udistrital.mdp.eventos.entities.bookingentity.NotificationEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.repositories.AssistantRepository;
import co.edu.udistrital.mdp.eventos.repositories.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AssistantNotificationService {

    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    /*
     * Asocia un Notification exitente a un Assisant
     * 
     * @param assistantId Identificador de instancia de Assistant
     * @param NotificationId Identificador de instancia de Notification
     * @return Intancia de NotificationEntity que fue asociada a un Assistant
     */

    @Transactional
    public NotificationEntity addNotification(Long assistantId, Long notificationId) throws EntityNotFoundException {
        log.info("Inicia el proceso de asociación de una notificación a un asistente con id = {}", assistantId);

        Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
        Optional<NotificationEntity> notificationEntity = notificationRepository.findById(notificationId);

        if (assistantEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        if (notificationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND);
        }

        AssistantEntity assistant = assistantEntity.get();
        NotificationEntity notification = notificationEntity.get();

        notification.setAssistant(assistant);
        assistant.getNotifications().add(notification);

        return notification;
    }

    /*
	 * Obtiene una colección de instancias de NotificationEntity asociadas a una instancia
	 * de Assistant
	 *
	 * @param assistantId Identificador de la instancia de Author
	 * @return Colección de instancias de NotificationEntity asociadas a la instancia de
	 * Assistant
	 */

    @Transactional
    public List<NotificationEntity> getNotifications(Long assistantId) throws EntityNotFoundException {
        log.info("Inicia el proceso de obtención de notificaciones de un asistente con id = {}", assistantId);

        Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);

        if (assistantEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        AssistantEntity assistant = assistantEntity.get();
        log.info("Termina el proceso de obtención de notificaciones de un asistente con id = {}", assistantId);

        return assistant.getNotifications();
    }

    /*
	 * Obtiene una instancia de NotificationEntity asociada a una instancia de Assistant
	 *
	 * @param assistantId Identificador de la instancia de Assistant
	 * @param NotificationId   Identificador de la instancia de Notification
	 * @return La entidadd de Notification del Assistant
	 */

    @Transactional
    public NotificationEntity getNotification(Long assistantId, Long notificationId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia el proceso de obtención de una notificación con id = {} de un asistente con id = {}", notificationId, assistantId);

        Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
        Optional<NotificationEntity> notification = notificationRepository.findById(notificationId);

        if (assistantEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        if (notification.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND);
        }
        log.info("Termina el proceso de obtención de una notificación con id = {} de un asistente con id = {}", notificationId, assistantId);

        if (notification.get().getAssistant() == null || !notification.get().getAssistant().getId().equals(assistantId)) {
            throw new IllegalOperationException("The notification is not associated to the assistant");
        }

        return notification.get();
    }

    /*
	 * Desasocia un Notification existente de un Assistant existente
	 *
	 * @param assistantId Identificador de la instancia de Assistant
	 * @param NotificationId   Identificador de la instancia de Notification
	 */

    @Transactional
    public void removeNotification(Long assistantId, Long notificationId) throws EntityNotFoundException {
        log.info("Inicia el proceso de desasociación de una notificación de un asistente con id = {}", assistantId);

        Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
        Optional<NotificationEntity> notificationEntity = notificationRepository.findById(notificationId);

        if (assistantEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        if (notificationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND);
        }
        
        NotificationEntity notification = notificationEntity.get();
        AssistantEntity assistant = assistantEntity.get();

        if (notification.getAssistant() == null || !notification.getAssistant().getId().equals(assistantId)) {
            throw new EntityNotFoundException(ErrorMessage.NOTIFICATION_NOT_ASSOCIATED);
        }

        assistant.getNotifications().remove(notification);
        notification.setAssistant(null);
        
        notificationRepository.save(notification);
        assistantRepository.save(assistant);
        log.info("Termina el proceso de desasociación de una notificación de un asistente con id = {}", assistantId);
    }
}
