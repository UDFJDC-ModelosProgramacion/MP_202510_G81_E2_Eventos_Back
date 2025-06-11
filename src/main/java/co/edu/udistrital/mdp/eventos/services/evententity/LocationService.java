package co.edu.udistrital.mdp.eventos.services.evententity;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.LocationEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.repositories.EventRespository;
import co.edu.udistrital.mdp.eventos.repositories.LocationRespository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationService {

    @Autowired
    private LocationRespository locationRepository;

    @Autowired
    private EventRespository eventRepository;

    @Transactional
    public LocationEntity createLocation(LocationEntity location) throws EntityNotFoundException {
        log.info("Inicia el proceso de creación de una ubicación");

        // Validaciones básicas
        if (location.getName() == null || location.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre de la ubicación es obligatorio.");
        }
        if (location.getLocation() == null || location.getLocation().isBlank()) {
        throw new IllegalArgumentException("La dirección física de la ubicación es obligatoria.");
        }
        if (location.getCapacity() == null || location.getCapacity() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser un número positivo.");
        }

        // Validar evento asociado (si existe)
        if (location.getEvent() != null && location.getEvent().getId() != null) {
            Optional<EventEntity> event = eventRepository.findById(location.getEvent().getId());
            if (event.isEmpty()) {
                throw new EntityNotFoundException("El evento asociado no existe.");
            }
            location.setEvent(event.get());
        }

        log.info("Ubicación creada con id={}", location.getId());
        return locationRepository.save(location);
    }

    @Transactional(readOnly = true)
    public List<LocationEntity> getAllLocations() {
        log.info("Inicia el proceso de obtención de todas las ubicaciones");
        return locationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public LocationEntity getLocationById(Long locationId) throws EntityNotFoundException {
        log.info("Inicia el proceso de obtención de una ubicación con id={}", locationId);
        return locationRepository.findById(locationId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND));
    }

    @Transactional
    public LocationEntity updateLocation(Long locationId, LocationEntity locationDetails) throws EntityNotFoundException {
        log.info("Inicia el proceso de actualización de una ubicación con id={}", locationId);
        
        LocationEntity location = locationRepository.findById(locationId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND));

        // Actualizar campos no-relacionales
        if (locationDetails.getName() != null) location.setName(locationDetails.getName());
        if (locationDetails.getLocation() != null) location.setLocation(locationDetails.getLocation());
        if (locationDetails.getType() != null) location.setType(locationDetails.getType());
        if (locationDetails.getCapacity() != null) {
            if (locationDetails.getCapacity() <= 0) {
                throw new IllegalArgumentException("La capacidad debe ser un número positivo.");
            }
            location.setCapacity(locationDetails.getCapacity());
        }

        // Actualizar evento asociado (si se proporciona)
        if (locationDetails.getEvent() != null && locationDetails.getEvent().getId() != null) {
            EventEntity event = eventRepository.findById(locationDetails.getEvent().getId())
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
            location.setEvent(event);
        } else {
            location.setEvent(null); // Permite desasociar el evento
        }

        return locationRepository.save(location);
    }

    @Transactional
    public void deleteLocation(Long locationId) throws EntityNotFoundException {
        log.info("Inicia el proceso de eliminación de una ubicación con id={}", locationId);
        if (!locationRepository.existsById(locationId)) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        locationRepository.deleteById(locationId);
    }

    // Métodos adicionales para búsquedas específicas
    @Transactional(readOnly = true)
    public List<LocationEntity> getLocationsByEvent(Long eventId) {
        log.info("Obteniendo ubicaciones del evento con id={}", eventId);
        return locationRepository.findByEventId(eventId);
    }

    @Transactional(readOnly = true)
    public List<LocationEntity> getLocationsByType(String type) {
        log.info("Obteniendo ubicaciones de tipo={}", type);
        return locationRepository.findByTypeContainingIgnoreCase(type);
    }
}