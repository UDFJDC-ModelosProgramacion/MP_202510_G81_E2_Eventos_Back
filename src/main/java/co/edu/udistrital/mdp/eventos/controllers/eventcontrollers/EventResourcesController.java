package co.edu.udistrital.mdp.eventos.controllers.eventcontrollers;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import co.edu.udistrital.mdp.eventos.dto.eventdto.ResourceDTO;
import co.edu.udistrital.mdp.eventos.entities.evententity.ResourceEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.evententity.EventService;
import co.edu.udistrital.mdp.eventos.services.evententity.ResourceService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

@RestController
@RequestMapping("/events/{eventId}/resources")
public class EventResourcesController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResourceDTO> getAllResourcesForEvent(@PathVariable Long eventId) throws EntityNotFoundException {
        List<ResourceEntity> resources = resourceService.getResourcesByEvent(eventId);
        return modelMapper.map(resources, new TypeToken<List<ResourceDTO>>() {}.getType());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceDTO createResourceForEvent(@PathVariable Long eventId, @RequestBody ResourceDTO resourceDTO) 
            throws EntityNotFoundException {
        resourceDTO.setEventId(eventId);
        ResourceEntity resourceEntity = resourceService.createResource(modelMapper.map(resourceDTO, ResourceEntity.class));
        return modelMapper.map(resourceEntity, ResourceDTO.class);
    }

    @GetMapping("/{resourceId}")
    @ResponseStatus(HttpStatus.OK)
    public ResourceDTO getResourceForEvent(
            @PathVariable Long eventId, 
            @PathVariable Long resourceId) throws EntityNotFoundException {
        ResourceEntity resource = resourceService.getResourceById(resourceId);
        if (!resource.getEvent().getId().equals(eventId)) {
            throw new EntityNotFoundException("El recurso no pertenece a este evento");
        }
        return modelMapper.map(resource, ResourceDTO.class);
    }

    @PutMapping("/{resourceId}")
    @ResponseStatus(HttpStatus.OK)
    public ResourceDTO updateResourceForEvent(
            @PathVariable Long eventId, 
            @PathVariable Long resourceId, 
            @RequestBody ResourceDTO resourceDTO) throws EntityNotFoundException {
        resourceDTO.setEventId(eventId);
        resourceDTO.setId(resourceId);
        ResourceEntity resourceEntity = resourceService.updateResource(
            resourceId, 
            modelMapper.map(resourceDTO, ResourceEntity.class)
        );
        return modelMapper.map(resourceEntity, ResourceDTO.class);
    }

    @DeleteMapping("/{resourceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResourceForEvent(
            @PathVariable Long eventId, 
            @PathVariable Long resourceId) throws EntityNotFoundException {
        ResourceEntity resource = resourceService.getResourceById(resourceId);
        if (!resource.getEvent().getId().equals(eventId)) {
            throw new EntityNotFoundException("El recurso no pertenece a este evento");
        }
        resourceService.deleteResource(resourceId);
    }
}