package co.edu.udistrital.mdp.eventos.services.evententity;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.ResourceEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.repositories.EventRespository;
import co.edu.udistrital.mdp.eventos.repositories.ResourceRespository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResourceService {

    @Autowired
    private ResourceRespository resourceRepository;

    @Autowired
    private EventRespository eventRepository;

    @Transactional
    public ResourceEntity createResource(ResourceEntity resource) throws EntityNotFoundException {
        log.info("Inicia el proceso de creación de un recurso");

        // Validaciones básicas
        if (resource.getUrl() == null || resource.getUrl().isBlank()) {
            throw new IllegalArgumentException("La URL del recurso es obligatoria.");
        }
        if (resource.getType() == null || resource.getType().isBlank()) {
            throw new IllegalArgumentException("El tipo de recurso es obligatorio.");
        }

        // Validar evento asociado (si existe)
        if (resource.getEvent() != null && resource.getEvent().getId() != null) {
            Optional<EventEntity> event = eventRepository.findById(resource.getEvent().getId());
            if (event.isEmpty()) {
                throw new EntityNotFoundException("El evento asociado no existe.");
            }
            resource.setEvent(event.get());
        }

        log.info("Recurso creado con id={}", resource.getId());
        return resourceRepository.save(resource);
    }

    @Transactional(readOnly = true)
    public List<ResourceEntity> getAllResources() {
        log.info("Inicia el proceso de obtención de todos los recursos");
        return resourceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ResourceEntity getResourceById(Long resourceId) throws EntityNotFoundException {
        log.info("Inicia el proceso de obtención de un recurso con id={}", resourceId);
        return resourceRepository.findById(resourceId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));
    }

    @Transactional
    public ResourceEntity updateResource(Long resourceId, ResourceEntity resourceDetails) throws EntityNotFoundException {
        log.info("Inicia el proceso de actualización de un recurso con id={}", resourceId);
        
        ResourceEntity resource = resourceRepository.findById(resourceId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));

        // Actualizar campos no-relacionales
        if (resourceDetails.getUrl() != null && !resourceDetails.getUrl().isBlank()) {
            resource.setUrl(resourceDetails.getUrl());
        }
        if (resourceDetails.getType() != null && !resourceDetails.getType().isBlank()) {
            resource.setType(resourceDetails.getType());
        }

        // Actualizar evento asociado (si se proporciona)
        if (resourceDetails.getEvent() != null && resourceDetails.getEvent().getId() != null) {
            EventEntity event = eventRepository.findById(resourceDetails.getEvent().getId())
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
            resource.setEvent(event);
        } else {
            resource.setEvent(null); // Permite desasociar el evento
        }

        return resourceRepository.save(resource);
    }

    @Transactional
    public void deleteResource(Long resourceId) throws EntityNotFoundException {
        log.info("Inicia el proceso de eliminación de un recurso con id={}", resourceId);
        if (!resourceRepository.existsById(resourceId)) {
            throw new EntityNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND);
        }
        resourceRepository.deleteById(resourceId);
    }

    // Métodos adicionales para búsquedas específicas
    @Transactional(readOnly = true)
    public List<ResourceEntity> getResourcesByEvent(Long eventId) {
        log.info("Obteniendo recursos del evento con id={}", eventId);
        return resourceRepository.findByEventId(eventId);
    }

    @Transactional(readOnly = true)
    public List<ResourceEntity> getResourcesByType(String type) {
        log.info("Obteniendo recursos de tipo={}", type);
        return resourceRepository.findByTypeContainingIgnoreCase(type);
    }
}