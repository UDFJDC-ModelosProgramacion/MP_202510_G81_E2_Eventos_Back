package co.edu.udistrital.mdp.eventos.services.evententity;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.LocationEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.repositories.EventRespository;
import co.edu.udistrital.mdp.eventos.repositories.LocationRespository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventLocationService {

    @Autowired
    private EventRespository eventRepository;

    @Autowired
    private LocationRespository locationRepository;

    @Transactional
    public LocationEntity assignLocationToEvent(Long eventId, Long locationId) 
        throws EntityNotFoundException, IllegalOperationException {
        
        log.info("Asignando ubicación {} al evento {}", locationId, eventId);
        
        Optional<EventEntity> eventOpt = eventRepository.findById(eventId);
        Optional<LocationEntity> locationOpt = locationRepository.findById(locationId);

        if (eventOpt.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND);
        }
        if (locationOpt.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }

        EventEntity event = eventOpt.get();
        LocationEntity location = locationOpt.get();

        // Verificar si ya tiene una ubicación asignada
        if (event.getLocation() != null) {
            throw new IllegalOperationException("El evento ya tiene una ubicación asignada");
        }

        event.setLocation(location);
        location.setEvent(event);
        
        eventRepository.save(event);
        return locationRepository.save(location);
    }

    @Transactional
    public LocationEntity getEventLocation(Long eventId) throws EntityNotFoundException {
        log.info("Obteniendo ubicación del evento {}", eventId);
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));
        
        if (event.getLocation() == null) {
            throw new EntityNotFoundException("El evento no tiene ubicación asignada");
        }
        
        return event.getLocation();
    }

    @Transactional
    public void removeLocationFromEvent(Long eventId) throws EntityNotFoundException {
        log.info("Removiendo ubicación del evento {}", eventId);
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));
        
        if (event.getLocation() == null) {
            throw new EntityNotFoundException("El evento no tiene ubicación asignada");
        }

        LocationEntity location = event.getLocation();
        event.setLocation(null);
        location.setEvent(null);
        
        eventRepository.save(event);
        locationRepository.save(location);
    }

    @Transactional
    public LocationEntity updateEventLocation(Long eventId, Long newLocationId) 
        throws EntityNotFoundException, IllegalOperationException {
        
        log.info("Actualizando ubicación del evento {} a {}", eventId, newLocationId);
        
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));
        
        LocationEntity newLocation = locationRepository.findById(newLocationId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND));

        // Desasociar la ubicación actual si existe
        if (event.getLocation() != null) {
            LocationEntity oldLocation = event.getLocation();
            oldLocation.setEvent(null);
            locationRepository.save(oldLocation);
        }

        // Asociar la nueva ubicación
        event.setLocation(newLocation);
        newLocation.setEvent(event);
        
        eventRepository.save(event);
        return locationRepository.save(newLocation);
    }
}