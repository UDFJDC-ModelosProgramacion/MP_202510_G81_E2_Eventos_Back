package co.edu.udistrital.mdp.eventos.controllers.eventcontrollers;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.eventos.dto.eventdto.*;
import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.OrganizerEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.evententity.EventService;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventDetailDTO> findAll() {
        List<EventEntity> events = eventService.getAllEvents();
        return modelMapper.map(events, new TypeToken<List<EventDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        EventEntity eventEntity = eventService.getEventById(id);
        return modelMapper.map(eventEntity, EventDetailDTO.class);
    }

@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public EventDTO create(@RequestBody EventDTO eventDTO) throws EntityNotFoundException {
    EventEntity eventEntity = modelMapper.map(eventDTO, EventEntity.class);

    // Convertir organizerId a OrganizerEntity
    if (eventDTO.getOrganizerId() != null) {
        OrganizerEntity organizer = new OrganizerEntity();
        organizer.setId(eventDTO.getOrganizerId());
        eventEntity.setOrganizer(organizer);
    }

    eventEntity = eventService.createEvent(eventEntity);
    return modelMapper.map(eventEntity, EventDTO.class);
}

@PutMapping("/{id}")
@ResponseStatus(HttpStatus.OK)
public EventDTO update(@PathVariable Long id, @RequestBody EventDTO eventDTO) 
        throws EntityNotFoundException {
    
    EventEntity eventEntity = modelMapper.map(eventDTO, EventEntity.class);

    if (eventDTO.getOrganizerId() != null) {
        OrganizerEntity organizer = new OrganizerEntity();
        organizer.setId(eventDTO.getOrganizerId());
        eventEntity.setOrganizer(organizer);
    }

    eventEntity = eventService.updateEvent(id, eventEntity);
    return modelMapper.map(eventEntity, EventDTO.class);
}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        eventService.deleteEvent(id);
    }
}