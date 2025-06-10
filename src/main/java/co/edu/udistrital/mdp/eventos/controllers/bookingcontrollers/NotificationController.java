package co.edu.udistrital.mdp.eventos.controllers.bookingcontrollers;

import co.edu.udistrital.mdp.eventos.dto.bookingdto.NotificationDTO;
import co.edu.udistrital.mdp.eventos.dto.bookingdto.NotificationDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.NotificationEntity;
import co.edu.udistrital.mdp.eventos.services.bookingentity.NotificationService;
import co.edu.udistrital.mdp.eventos.exceptions.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationDetailDTO> findAll() {
        List<NotificationEntity> entities = notificationService.getNotifications();
        return modelMapper.map(entities, new TypeToken<List<NotificationDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        NotificationEntity entity = notificationService.getNotification(id);
        return modelMapper.map(entity, NotificationDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationDTO create(@RequestBody NotificationDTO dto) throws IllegalOperationException {
        NotificationEntity entity = notificationService.createNotification(modelMapper.map(dto, NotificationEntity.class));
        return modelMapper.map(entity, NotificationDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationDTO update(@PathVariable Long id, @RequestBody NotificationDTO dto) throws EntityNotFoundException {
        NotificationEntity entity = notificationService.updateNotification(id, modelMapper.map(dto, NotificationEntity.class));
        return modelMapper.map(entity, NotificationDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        notificationService.deleteNotification(id);
    }
}