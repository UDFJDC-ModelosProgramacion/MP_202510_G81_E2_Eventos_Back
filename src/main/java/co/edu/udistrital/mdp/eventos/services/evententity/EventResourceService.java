package co.edu.udistrital.mdp.eventos.services.evententity;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.ResourceEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.repositories.EventRespository;
import co.edu.udistrital.mdp.eventos.repositories.ResourceRespository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventResourceService {

    @Autowired
    private EventRespository eventRepository;

    @Autowired
    private ResourceRespository resourceRepository;

    @Transactional
    public ResourceEntity addResourceToEvent(Long eventId, Long resourceId) 
        throws EntityNotFoundException {
        
        log.info("Agregando recurso {} al evento {}", resourceId, eventId);
        
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));
        
        ResourceEntity resource = resourceRepository.findById(resourceId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));

        resource.setEvent(event);
        event.getResource().add(resource);
        
        eventRepository.save(event);
        return resourceRepository.save(resource);
    }

    @Transactional(readOnly = true)
    public List<ResourceEntity> getEventResources(Long eventId) throws EntityNotFoundException {
        log.info("Obteniendo recursos del evento {}", eventId);
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));
        
        return event.getResource();
    }

    @Transactional
    public ResourceEntity getEventResource(Long eventId, Long resourceId) 
        throws EntityNotFoundException, IllegalOperationException {
        
        log.info("Obteniendo recurso {} del evento {}", resourceId, eventId);
        
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));
        
        ResourceEntity resource = resourceRepository.findById(resourceId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));

        if (!event.getResource().contains(resource)) {
            throw new IllegalOperationException("El recurso no está asociado a este evento");
        }
        
        return resource;
    }

    @Transactional
    public void removeResourceFromEvent(Long eventId, Long resourceId) 
        throws EntityNotFoundException, IllegalOperationException {
        
        log.info("Eliminando recurso {} del evento {}", resourceId, eventId);
        
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));
        
        ResourceEntity resource = resourceRepository.findById(resourceId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));

        if (!event.getResource().contains(resource)) {
            throw new IllegalOperationException("El recurso no está asociado a este evento");
        }

        event.getResource().remove(resource);
        resource.setEvent(null);
        
        eventRepository.save(event);
        resourceRepository.save(resource);
    }

    @Transactional
    public List<ResourceEntity> replaceEventResources(Long eventId, List<ResourceEntity> resources) 
        throws EntityNotFoundException {
        
        log.info("Reemplazando recursos del evento {}", eventId);
        
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));

        // Validar que todos los recursos existan
        for (ResourceEntity resource : resources) {
            if (!resourceRepository.existsById(resource.getId())) {
                throw new EntityNotFoundException("Recurso con id " + resource.getId() + " no encontrado");
            }
        }

        // Desasociar recursos actuales
        for (ResourceEntity existingResource : event.getResource()) {
            existingResource.setEvent(null);
            resourceRepository.save(existingResource);
        }

        // Asociar nuevos recursos
        List<ResourceEntity> newResources = resources.stream()
            .map(r -> {
                ResourceEntity resource = resourceRepository.findById(r.getId()).get();
                resource.setEvent(event);
                return resourceRepository.save(resource);
            })
            .collect(Collectors.toList());

        event.setResource(newResources);
        eventRepository.save(event);
        
        return newResources;
    }
}