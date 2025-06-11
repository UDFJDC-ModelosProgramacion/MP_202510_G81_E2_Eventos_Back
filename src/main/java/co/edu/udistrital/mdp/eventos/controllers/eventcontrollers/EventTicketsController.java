package co.edu.udistrital.mdp.eventos.controllers.eventcontrollers;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import co.edu.udistrital.mdp.eventos.dto.eventdto.TicketDTO;
import co.edu.udistrital.mdp.eventos.dto.eventdto.TicketDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.evententity.TicketEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.evententity.EventService;
import co.edu.udistrital.mdp.eventos.services.evententity.TicketService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

@RestController
@RequestMapping("/events/{eventId}/tickets")
public class EventTicketsController {

    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TicketDTO> getAllTicketsForEvent(@PathVariable Long eventId) throws EntityNotFoundException {
        List<TicketEntity> tickets = ticketService.getTicketsByEvent(eventId);
        return modelMapper.map(tickets, new TypeToken<List<TicketDTO>>() {}.getType());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDTO createTicketForEvent(
            @PathVariable Long eventId, 
            @RequestBody TicketDTO ticketDTO) throws EntityNotFoundException, IllegalArgumentException {
        ticketDTO.setEventId(eventId);
        TicketEntity ticketEntity = ticketService.createTicket(modelMapper.map(ticketDTO, TicketEntity.class));
        return modelMapper.map(ticketEntity, TicketDTO.class);
    }

    @GetMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    public TicketDetailDTO getTicketForEvent(
            @PathVariable Long eventId, 
            @PathVariable Long ticketId) throws EntityNotFoundException {
        TicketEntity ticket = ticketService.getTicketById(ticketId);
        if (!ticket.getEvent().getId().equals(eventId)) {
            throw new EntityNotFoundException("El ticket no pertenece a este evento");
        }
        return modelMapper.map(ticket, TicketDetailDTO.class);
    }

    @PutMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    public TicketDTO updateTicketForEvent(
            @PathVariable Long eventId, 
            @PathVariable Long ticketId, 
            @RequestBody TicketDTO ticketDTO) throws EntityNotFoundException, IllegalArgumentException {
        ticketDTO.setEventId(eventId);
        ticketDTO.setId(ticketId);
        TicketEntity ticketEntity = ticketService.updateTicket(
            ticketId, 
            modelMapper.map(ticketDTO, TicketEntity.class)
        );
        return modelMapper.map(ticketEntity, TicketDTO.class);
    }

    @DeleteMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicketForEvent(
            @PathVariable Long eventId, 
            @PathVariable Long ticketId) throws EntityNotFoundException {
        TicketEntity ticket = ticketService.getTicketById(ticketId);
        if (!ticket.getEvent().getId().equals(eventId)) {
            throw new EntityNotFoundException("El ticket no pertenece a este evento");
        }
        ticketService.deleteTicket(ticketId);
    }

    @PatchMapping("/{ticketId}/availability")
    @ResponseStatus(HttpStatus.OK)
    public TicketDTO updateTicketAvailability(
            @PathVariable Long eventId,
            @PathVariable Long ticketId,
            @RequestParam Integer quantity) throws EntityNotFoundException, IllegalArgumentException {
        TicketEntity ticket = ticketService.getTicketById(ticketId);
        if (!ticket.getEvent().getId().equals(eventId)) {
            throw new EntityNotFoundException("El ticket no pertenece a este evento");
        }
        TicketEntity updatedTicket = ticketService.updateTicketAvailability(ticketId, quantity);
        return modelMapper.map(updatedTicket, TicketDTO.class);
    }
}