package co.edu.udistrital.mdp.eventos.services.userentity.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.eventos.repositories.AssistantRepository;
import co.edu.udistrital.mdp.eventos.repositories.NotificationRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssistantNotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private AssistantRepository assistantRepository;
}
