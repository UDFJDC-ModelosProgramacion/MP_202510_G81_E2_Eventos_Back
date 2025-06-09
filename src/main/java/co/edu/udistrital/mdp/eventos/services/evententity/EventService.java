package co.edu.udistrital.mdp.eventos.services.evententity;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.OrganizerEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.repositories.EventRespository;
import co.edu.udistrital.mdp.eventos.repositories.OrganizerRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventService {

    @Autowired
    private EventRespository eventRepository;

    @Autowired
    private OrganizerRepository organizerRepository;

    @Transactional
    public EventEntity createEvent(EventEntity event) throws EntityNotFoundException {
        log.info("Inicia el proceso de creación de un evento");

        
        if (event.getName() == null || event.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del evento es obligatorio.");
        }
        if (event.getDate() == null) {
            throw new IllegalArgumentException("La fecha del evento es obligatoria.");
        }

        // Validar organizador
        if (event.getOrganizer() != null && event.getOrganizer().getId() != null) {
            Optional<OrganizerEntity> organizer = organizerRepository.findById(event.getOrganizer().getId());
            if (organizer.isEmpty()) {
                throw new EntityNotFoundException("El organizador asociado no existe.");
            }
            event.setOrganizer(organizer.get());
        }

        log.info("Evento creado con id={}", event.getId());
        return eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public List<EventEntity> getAllEvents() {
        log.info("Inicia el proceso de obtención de todos los eventos");
        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public EventEntity getEventById(Long eventId) throws EntityNotFoundException {
        log.info("Inicia el proceso de obtención de un evento con id={}", eventId);
        return eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));
    }

    @Transactional
    public EventEntity updateEvent(Long eventId, EventEntity eventDetails) throws EntityNotFoundException {
        log.info("Inicia el proceso de actualización de un evento con id={}", eventId);
        
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));

        // Actualizar campos no-relacionales
        if (eventDetails.getName() != null) event.setName(eventDetails.getName());
        if (eventDetails.getDescription() != null) event.setDescription(eventDetails.getDescription());
        if (eventDetails.getCategory() != null) event.setCategory(eventDetails.getCategory());
        if (eventDetails.getDate() != null) event.setDate(eventDetails.getDate());

        // Actualizar organizador (si se proporciona)
        if (eventDetails.getOrganizer() != null && eventDetails.getOrganizer().getId() != null) {
            OrganizerEntity organizer = organizerRepository.findById(eventDetails.getOrganizer().getId())
                .orElseThrow(() -> new EntityNotFoundException("Organizador no encontrado"));
            event.setOrganizer(organizer);
        }

        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long eventId) throws EntityNotFoundException {
        log.info("Inicia el proceso de eliminación de un evento con id={}", eventId);
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND);
        }
        eventRepository.deleteById(eventId);
    }

    // Métodos adicionales para búsquedas específicas
    @Transactional(readOnly = true)
    public List<EventEntity> getEventsByOrganizer(Long organizerId) {
        log.info("Obteniendo eventos del organizador con id={}", organizerId);
        return eventRepository.findByOrganizerId(organizerId);
    }
}