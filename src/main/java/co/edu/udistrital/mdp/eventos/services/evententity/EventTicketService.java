package co.edu.udistrital.mdp.eventos.services.evententity;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.TicketEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.repositories.EventRespository;
import co.edu.udistrital.mdp.eventos.repositories.TicketRespository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventTicketService {

    @Autowired
    private EventRespository eventRepository;

    @Autowired
    private TicketRespository ticketRepository;

    @Transactional
    public TicketEntity addTicketToEvent(Long eventId, Long ticketId) 
        throws EntityNotFoundException, IllegalOperationException {
        
        log.info("Agregando ticket {} al evento {}", ticketId, eventId);
        
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));
        
        TicketEntity ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TICKET_NOT_FOUND));

        if (ticket.getEvent() != null && !ticket.getEvent().getId().equals(eventId)) {
            throw new IllegalOperationException("El ticket ya está asociado a otro evento");
        }

        ticket.setEvent(event);
        event.getTickets().add(ticket);
        
        eventRepository.save(event);
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketEntity> getEventTickets(Long eventId) throws EntityNotFoundException {
        log.info("Obteniendo tickets del evento {}", eventId);
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));
        
        return event.getTickets();
    }

    @Transactional
    public TicketEntity getEventTicket(Long eventId, Long ticketId) 
        throws EntityNotFoundException, IllegalOperationException {
        
        log.info("Obteniendo ticket {} del evento {}", ticketId, eventId);
        
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));
        
        TicketEntity ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TICKET_NOT_FOUND));

        if (!event.getTickets().contains(ticket)) {
            throw new IllegalOperationException("El ticket no está asociado a este evento");
        }
        
        return ticket;
    }

    @Transactional
    public void removeTicketFromEvent(Long eventId, Long ticketId) 
        throws EntityNotFoundException, IllegalOperationException {
        
        log.info("Eliminando ticket {} del evento {}", ticketId, eventId);
        
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));
        
        TicketEntity ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TICKET_NOT_FOUND));

        if (!event.getTickets().contains(ticket)) {
            throw new IllegalOperationException("El ticket no está asociado a este evento");
        }

        event.getTickets().remove(ticket);
        ticket.setEvent(null);
        
        eventRepository.save(event);
        ticketRepository.save(ticket);
    }

    @Transactional
    public List<TicketEntity> replaceEventTickets(Long eventId, List<TicketEntity> tickets) 
        throws EntityNotFoundException {
        
        log.info("Reemplazando tickets del evento {}", eventId);
        
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));

        // Validar que todos los tickets existan
        for (TicketEntity ticket : tickets) {
            if (!ticketRepository.existsById(ticket.getId())) {
                throw new EntityNotFoundException("Ticket con id " + ticket.getId() + " no encontrado");
            }
        }

        // Desasociar tickets actuales
        for (TicketEntity existingTicket : event.getTickets()) {
            existingTicket.setEvent(null);
            ticketRepository.save(existingTicket);
        }

        // Asociar nuevos tickets
        List<TicketEntity> newTickets = tickets.stream()
            .map(t -> {
                TicketEntity ticket = ticketRepository.findById(t.getId()).get();
                ticket.setEvent(event);
                return ticketRepository.save(ticket);
            })
            .collect(Collectors.toList());

        event.setTickets(newTickets);
        eventRepository.save(event);
        
        return newTickets;
    }

    @Transactional
    public List<TicketEntity> createAndAddTicketsToEvent(Long eventId, List<TicketEntity> newTickets) 
        throws EntityNotFoundException {
        
        log.info("Creando y agregando nuevos tickets al evento {}", eventId);
        
        EventEntity event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.EVENT_NOT_FOUND));

        for (TicketEntity ticket : newTickets) {
            ticket.setEvent(event);
            ticketRepository.save(ticket);
            event.getTickets().add(ticket);
        }

        eventRepository.save(event);
        return newTickets;
    }
}