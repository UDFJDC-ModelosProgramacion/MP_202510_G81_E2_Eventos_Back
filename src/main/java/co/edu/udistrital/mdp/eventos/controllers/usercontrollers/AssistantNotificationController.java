package co.edu.udistrital.mdp.eventos.controllers.usercontrollers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.eventos.dto.bookingdto.NotificationDetailDTO;
import co.edu.udistrital.mdp.eventos.dto.userdto.PreferenceDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.NotificationEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.userentity.booking.AssistantNotificationService;



@RestController
@RequestMapping("/assistants")
public class AssistantNotificationController {

    @Autowired
    private AssistantNotificationService assistantNotificationService;

    @Autowired
    private ModelMapper modelMapper;

    /**
	 * Asocia un Notification existente con un assistant existente
	 *
	 * @param assistantId El ID del assitant al cual se le va a asociar el libro
	 * @param NotificationId   El ID del Notification que se asocia
	 * @return JSON {@link PreferenceDetailDTO} - El libro asociado. 
	 */
	@PostMapping(value = "/{assistantId}/notifications/{NotificationId}")
	@ResponseStatus(code = HttpStatus.OK)
	public NotificationDetailDTO addNotification(@PathVariable Long assistantId, @PathVariable Long NotificationId)
			throws EntityNotFoundException {
		NotificationEntity notificationEntity = assistantNotificationService.addNotification(assistantId, NotificationId);
		return modelMapper.map(notificationEntity, NotificationDetailDTO.class);
	}

	/**
	 * Busca y devuelve la Notification con el ID recibido en la URL, relativo a un asistente.
	 *
	 * @param assistantId El ID del asistant del cual se busca la preference
	 * @param NotificationId   El ID de la Notification que se busca
	 * @return {@link NotificationDetailDTO} - El Notification encontrado en el assistant.
	 */
	@GetMapping(value = "/{assistantId}/notifications/{NotificationId}")
	@ResponseStatus(code = HttpStatus.OK)
	public NotificationDetailDTO getNotification(@PathVariable Long assistantId, @PathVariable Long NotificationId) throws EntityNotFoundException, IllegalOperationException {
		NotificationEntity notificationEntity = assistantNotificationService.getNotification(assistantId, NotificationId);
		return modelMapper.map(notificationEntity, NotificationDetailDTO.class);
	}

		/**
	 * Busca y devuelve todos los notifications que existen en un assistant.
	 *
	 * @param assistantId El ID del asistant del cual se busca la preference
	 * @param NotificationId   El ID de la Notification que se busca
	 * @return {@link NotificationDetailDTO} - El Notification encontrado en el assistant.
	 */
	@GetMapping(value = "/{assistantId}/notifications")
	@ResponseStatus(code = HttpStatus.OK)
	public List<NotificationDetailDTO> getnotifications(@PathVariable Long assistantId) throws EntityNotFoundException {
		List<NotificationEntity> NotificationEntity = assistantNotificationService.getNotifications(assistantId);
		return modelMapper.map(NotificationEntity, new TypeToken<List<NotificationDetailDTO>>() {}.getType());
	}

	/**
	 * Elimina la conexión entre el Notification y el assistant recibidos en la URL.
	 *
	 * @param assistantId El ID del assistant al cual se le va a desasociar el Notification
	 * @param NotificationId   El ID del Notification que se desasocia
	 */
	@DeleteMapping(value = "/{assistantId}/notifications/{NotificationId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeNotification(@PathVariable Long assistantId, @PathVariable Long NotificationId)
			throws EntityNotFoundException {
		assistantNotificationService.removeNotification(assistantId, NotificationId);
	}
}
