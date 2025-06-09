package co.edu.udistrital.mdp.eventos.services.evententity;


//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
//import co.edu.udistrital.mdp.eventos.entities.evententity.LocationEntity;
//import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
//import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
//import co.edu.udistrital.mdp.eventos.repositories.EventRespository;
//import co.edu.udistrital.mdp.eventos.repositories.LocationRespository;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service
//public class LocationService {
//
//    @Autowired
//    private LocationRespository locationRepository;
//
//    @Autowired
//    private EventRespository eventRepository;
//
//    @Transactional
//    public LocationEntity createLocation(LocationEntity location) throws EntityNotFoundException {
//        log.info("Inicia el proceso de creación de una ubicación");
//
//        // Validaciones básicas
//        if (location.getName() == null || location.getName().isBlank()) {
//            throw new IllegalArgumentException("El nombre de la ubicación es obligatorio.");
//        }
//        if (location.getLocation() == null || location.getLocation().isBlank()) {
//            throw new IllegalArgumentException("La dirección física de la ubicación es obligatoria.");
//        }
//        if (location.getCapacity() == null || location.getCapacity() <= 0) {
//            throw new IllegalArgumentException("La capacidad debe ser un número positivo.");
//        }
//
//        // Validar evento asociado (si existe)
//        if (location.getEvent() != null && location.getEvent().getId() != null) {
//            Optional<EventEntity> event = eventRepository.findById(location.getEvent().getId());
//            if (event.isEmpty()) {
//                throw new EntityNotFoundException("El evento asociado no existe.");
//            }
//            location.setEvent(event.get());
//        }
//
//        log.info("Ubicación creada con id={}", location.getId());
//        return locationRepository.save(location);
//    }
//
//    @Transactional(readOnly = true)
//    public List<LocationEntity> getAllLocations() {
//        log.info("Inicia el proceso de obtención de todas las ubicaciones");
//        return locationRepository.findAll();
//    }
//
//    @Transactional(readOnly = true)
//    public LocationEntity getLocationById(Long locationId) throws EntityNotFoundException {
//        log.info("Inicia el proceso de obtención de una ubicación con id={}", locationId);
//        return locationRepository.findById(locationId)
//            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND));
//    }
//
//    @Transactional
//    public LocationEntity updateLocation(Long locationId, LocationEntity locationDetails) throws EntityNotFoundException {
//        log.info("Inicia el proceso de actualización de una ubicación con id={}", locationId);
//        
//        LocationEntity location = locationRepository.findById(locationId)
//            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND));
//
//        // Actualizar campos no-relacionales
//        if (locationDetails.getName() != null) location.setName(locationDetails.getName());
//        if (locationDetails.getLocation() != null) location.setLocation(locationDetails.getLocation());
//        if (locationDetails.getType() != null) location.setType(locationDetails.getType());
//        if (locationDetails.getCapacity() != null) {
//            if (locationDetails.getCapacity() <= 0) {
//                throw new IllegalArgumentException("La capacidad debe ser un número positivo.");
//            }
//            location.setCapacity(locationDetails.getCapacity());
//        }
//
//        // Actualizar evento asociado (si se proporciona)
//        if (locationDetails.getEvent() != null && locationDetails.getEvent().getId() != null) {
//            EventEntity event = eventRepository.findById(locationDetails.getEvent().getId())
//                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
//            location.setEvent(event);
//        } else {
//            location.setEvent(null); // Permite desasociar el evento
//        }
//
//        return locationRepository.save(location);
//    }
//
//    @Transactional
//    public void deleteLocation(Long locationId) throws EntityNotFoundException {
//        log.info("Inicia el proceso de eliminación de una ubicación con id={}", locationId);
//        if (!locationRepository.existsById(locationId)) {
//            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
//        }
//        locationRepository.deleteById(locationId);
//    }
//
//    // Métodos adicionales para búsquedas específicas
//    @Transactional(readOnly = true)
//    public List<LocationEntity> getLocationsByEvent(Long eventId) {
//        log.info("Obteniendo ubicaciones del evento con id={}", eventId);
//        return locationRepository.findByEventId(eventId);
//    }
//
//    @Transactional(readOnly = true)
//    public List<LocationEntity> getLocationsByType(String type) {
//        log.info("Obteniendo ubicaciones de tipo={}", type);
//        return locationRepository.findByTypeContainingIgnoreCase(type);
//    }
//}
//package co.edu.udistrital.mdp.eventos.services;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.LocationEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
//import co.edu.udistrital.mdp.eventos.repositories.EventRepository;
//import co.edu.udistrital.mdp.eventos.repositories.LocationRepository;
import co.edu.udistrital.mdp.eventos.repositories.LocationRespository;
import co.edu.udistrital.mdp.eventos.repositories.EventRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRespository locationRepository;

    @Autowired
    private EventRespository eventRepository;

    public LocationEntity createLocation(LocationEntity location) throws EntityNotFoundException {
        if (location.getLocation() == null) {
            throw new IllegalArgumentException("La dirección de la locación no puede ser nula.");
        }

        if (location.getEvent() != null) {
            Long eventId = location.getEvent().getId();
            EventEntity event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con id: " + eventId));

            // Asegurar que el evento no tenga ya una locación asociada
            if (event.getLocation() != null) {
                throw new IllegalArgumentException("El evento ya tiene una locación asociada.");
            }

            location.setEvent(event);
            event.setLocation(location);
        }

        return locationRepository.save(location);
    }

    public LocationEntity getLocationById(Long id) throws EntityNotFoundException {
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Locación no encontrada con id: " + id));
    }

    public LocationEntity updateLocation(Long id, LocationEntity updates) throws EntityNotFoundException {
        LocationEntity existing = getLocationById(id);

        if (updates.getName() != null) existing.setName(updates.getName());
        if (updates.getLocation() != null) existing.setLocation(updates.getLocation());
        if (updates.getType() != null) existing.setType(updates.getType());
        if (updates.getCapacity() != null) existing.setCapacity(updates.getCapacity());

        return locationRepository.save(existing);
    }

    public LocationEntity getLocationByEvent(Long eventId) throws EntityNotFoundException {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con id: " + eventId));

        LocationEntity location = event.getLocation();
        if (location == null) {
            throw new EntityNotFoundException("No se encontró una locación para el evento con id: " + eventId);
        }

        return location;
    }

    public List<LocationEntity> getAllLocations() {
    return locationRepository.findAll();
}

public void deleteLocation(Long id) throws EntityNotFoundException {
    LocationEntity location = getLocationById(id);
    locationRepository.delete(location);
}

}
