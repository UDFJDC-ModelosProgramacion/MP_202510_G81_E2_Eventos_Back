package co.edu.udistrital.mdp.eventos.controllers.eventcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import co.edu.udistrital.mdp.eventos.dto.eventdto.LocationDTO;
import co.edu.udistrital.mdp.eventos.entities.evententity.LocationEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.evententity.EventService;
import co.edu.udistrital.mdp.eventos.services.evententity.LocationService;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/events/{eventId}/location")
public class EventLocationController {

    @Autowired
    private EventService eventService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public LocationDTO getLocationForEvent(@PathVariable Long eventId) throws EntityNotFoundException {
        LocationEntity location = locationService.getLocationById(eventId);
        return modelMapper.map(location, LocationDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDTO createLocationForEvent(@PathVariable Long eventId, @RequestBody LocationDTO locationDTO) 
            throws EntityNotFoundException {
        locationDTO.setEventId(eventId);
        LocationEntity locationEntity = locationService.createLocation(modelMapper.map(locationDTO, LocationEntity.class));
        return modelMapper.map(locationEntity, LocationDTO.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public LocationDTO updateLocationForEvent(@PathVariable Long eventId, @RequestBody LocationDTO locationDTO) 
            throws EntityNotFoundException {
        locationDTO.setEventId(eventId);
        LocationEntity existingLocation = locationService.getLocationById(eventId);
        LocationEntity updatedLocation = locationService.updateLocation(
            existingLocation.getId(), 
            modelMapper.map(locationDTO, LocationEntity.class)
        );
        return modelMapper.map(updatedLocation, LocationDTO.class);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocationForEvent(@PathVariable Long eventId) throws EntityNotFoundException {
        LocationEntity location = locationService.getLocationById(eventId);
        locationService.deleteLocation(location.getId());
    }
}