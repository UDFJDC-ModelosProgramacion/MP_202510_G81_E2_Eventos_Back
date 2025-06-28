package co.edu.udistrital.mdp.eventos.services.users;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.NotificationEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.userentity.booking.AssistantNotificationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(AssistantNotificationService.class)
public class AssistantNotificationServiceTest {

    @Autowired
    private AssistantNotificationService assistantNotificationService;
    
    @Autowired
    private TestEntityManager entityManager;
    
    // Listas para almacenar los datos insertados
    private List<AssistantEntity> assistantData = new ArrayList<>();
    private List<NotificationEntity> notificationData = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }
    
    /*
     * Borra los datos de las tablas implicadas en las pruebas.
     */
    private void clearData() {
        entityManager.getEntityManager()
                    .createQuery("DELETE FROM NotificationEntity")
                    .executeUpdate();
        entityManager.getEntityManager()
                    .createQuery("DELETE FROM AssistantEntity")
                    .executeUpdate();
    }
    
    /**
     * Inserta datos iniciales para las pruebas.
     */
    private void insertData() {
        // Creamos dos asistentes para pruebas
        AssistantEntity assistant1 = new AssistantEntity();
        assistant1.setName("Assistant 1");
        entityManager.persist(assistant1);
        assistantData.add(assistant1);
        
        AssistantEntity assistant2 = new AssistantEntity();
        assistant2.setName("Assistant 2");
        entityManager.persist(assistant2);
        assistantData.add(assistant2);
        
        // Creamos tres notificaciones
        // Notificación asociada al assistant1
        NotificationEntity notification1 = new NotificationEntity();
        notification1.setDescription("Notification 1");
        notification1.setAssistant(assistant1);
        entityManager.persist(notification1);
        notificationData.add(notification1);
        // Agregamos la notificación a la lista del assistant1
        assistant1.getNotifications().add(notification1);
        
        // Notificación sin asociación
        NotificationEntity notification2 = new NotificationEntity();
        notification2.setDescription("Notification 2");
        // no se asocia aún a ningún assistant
        entityManager.persist(notification2);
        notificationData.add(notification2);
        
        // Notificación asociada al assistant2 (para pruebas de no asociación)
        NotificationEntity notification3 = new NotificationEntity();
        notification3.setDescription("Notification 3");
        notification3.setAssistant(assistant2);
        entityManager.persist(notification3);
        notificationData.add(notification3);
        assistant2.getNotifications().add(notification3);
        
        // Se hace flush a la data insertada
        entityManager.flush();
    }
    
    // ------------------ TESTS PARA addNotification ------------------
    
    @Test
    public void testAddNotificationValid() throws Exception {
        // Asociar la notificación 2 (que no tiene assistant) al assistant1
        Long assistantId = assistantData.get(0).getId();
        Long notificationId = notificationData.get(1).getId();
        
        NotificationEntity result = assistantNotificationService.addNotification(assistantId, notificationId);
        assertNotNull(result);
        assertEquals(assistantId, result.getAssistant().getId(), "La notificación debe estar asociada al assistant1");
    }
    
    @Test
    public void testAddNotificationInvalidAssistant() {
        // Intentar asociar una notificación con un assistant inexistente
        Long invalidAssistantId = 999L;
        Long notificationId = notificationData.get(1).getId();
        
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantNotificationService.addNotification(invalidAssistantId, notificationId);
        });
        assertEquals(ErrorMessage.ASSISTANT_NOT_FOUND, exception.getMessage());
    }
    
    @Test
    public void testAddNotificationInvalidNotification() {
        // Intentar asociar notificación inexistente a un assistant existente
        Long assistantId = assistantData.get(0).getId();
        Long invalidNotificationId = 999L;
        
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantNotificationService.addNotification(assistantId, invalidNotificationId);
        });
        assertEquals(ErrorMessage.NOTIFICATION_NOT_FOUND, exception.getMessage());
    }
    
    // ------------------ TESTS PARA getNotifications ------------------
    
    @Test
    public void testGetNotificationsValid() throws Exception {
        // El assistant1 tiene una notificación asociada (notification1)
        Long assistantId = assistantData.get(0).getId();
        List<NotificationEntity> notifications = assistantNotificationService.getNotifications(assistantId);
        assertNotNull(notifications);
        assertFalse(notifications.isEmpty(), "Debe devolver al menos una notificación");
        // Verificar que el id del assistant en cada notificación sea el mismo
        for (NotificationEntity notification : notifications) {
            assertEquals(assistantId, notification.getAssistant().getId());
        }
    }
    
    @Test
    public void testGetNotificationsInvalidAssistant() {
        Long invalidAssistantId = 999L;
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantNotificationService.getNotifications(invalidAssistantId);
        });
        assertEquals(ErrorMessage.ASSISTANT_NOT_FOUND, exception.getMessage());
    }
    
    // ------------------ TESTS PARA getNotification ------------------
    
    @Test
    public void testGetNotificationValid() throws Exception {
        // Se prueba la obtención de la notificación asociada al assistant1 (notification1)
        Long assistantId = assistantData.get(0).getId();
        Long notificationId = notificationData.get(0).getId();
        
        NotificationEntity result = assistantNotificationService.getNotification(assistantId, notificationId);
        assertNotNull(result);
        assertEquals(assistantId, result.getAssistant().getId());
    }
    
    @Test
    public void testGetNotificationAssistantNotFound() {
        Long invalidAssistantId = 999L;
        Long notificationId = notificationData.get(0).getId();
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantNotificationService.getNotification(invalidAssistantId, notificationId);
        });
        assertEquals(ErrorMessage.ASSISTANT_NOT_FOUND, exception.getMessage());
    }
    
    @Test
    public void testGetNotificationNotificationNotFound() {
        Long assistantId = assistantData.get(0).getId();
        Long invalidNotificationId = 999L;
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantNotificationService.getNotification(assistantId, invalidNotificationId);
        });
        assertEquals(ErrorMessage.NOTIFICATION_NOT_FOUND, exception.getMessage());
    }
    
    @Test
    public void testGetNotificationNotAssociated() {
        // Se prueba cuando la notificación está asociada a otro assistant.
        // notification3 está asociado a assistant2. Se intenta obtenerlo usando assistant1.
        Long assistant1Id = assistantData.get(0).getId();
        Long notification3Id = notificationData.get(2).getId();
        
        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            assistantNotificationService.getNotification(assistant1Id, notification3Id);
        });
        assertEquals("The notification is not associated to the assistant", exception.getMessage());
    }
    
    // ------------------ TESTS PARA removeNotification ------------------
    
    @Test
    public void testRemoveNotificationValid() throws Exception {
        // removeNotification debe desasociar notification1 del assistant1.
        AssistantEntity assistant = assistantData.get(0);
        NotificationEntity notification = notificationData.get(0);
        Long assistantId = assistant.getId();
        Long notificationId = notification.getId();
        
        // Verifica que inicialmente la notificación esté asociada
        assertEquals(assistantId, notification.getAssistant().getId());
        assertTrue(assistant.getNotifications().contains(notification));
        
        // Remueve la asociación
        assistantNotificationService.removeNotification(assistantId, notificationId);
        
        // Se refresca la entidad para verificar los cambios
        NotificationEntity removed = entityManager.find(NotificationEntity.class, notificationId);
        AssistantEntity updatedAssistant = entityManager.find(AssistantEntity.class, assistantId);
        
        assertNull(removed.getAssistant(), "La notificación ya no debe tener assistant asociado");
        assertFalse(updatedAssistant.getNotifications().contains(removed), "El assistant ya no debe listar la notificación");
    }
    
    @Test
    public void testRemoveNotificationAssistantNotFound() {
        Long invalidAssistantId = 999L;
        Long notificationId = notificationData.get(0).getId();
        
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantNotificationService.removeNotification(invalidAssistantId, notificationId);
        });
        assertEquals(ErrorMessage.ASSISTANT_NOT_FOUND, exception.getMessage());
    }
    
    @Test
    public void testRemoveNotificationNotificationNotFound() {
        Long assistantId = assistantData.get(0).getId();
        Long invalidNotificationId = 999L;
        
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantNotificationService.removeNotification(assistantId, invalidNotificationId);
        });
        assertEquals(ErrorMessage.NOTIFICATION_NOT_FOUND, exception.getMessage());
    }
    
    @Test
    public void testRemoveNotificationNotAssociated() {
        // Se intenta remover una notificación que no está asociada al assistant1.
        // Por ejemplo, notification3 está asociada a assistant2.
        Long assistant1Id = assistantData.get(0).getId();
        Long notification3Id = notificationData.get(2).getId();
        
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantNotificationService.removeNotification(assistant1Id, notification3Id);
        });
        assertEquals(ErrorMessage.NOTIFICATION_NOT_ASSOCIATED, exception.getMessage());
    }
}
