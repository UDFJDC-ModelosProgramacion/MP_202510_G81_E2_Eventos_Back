package co.edu.udistrital.mdp.eventos.services.evententity;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.TicketEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.repositories.EventRespository;
import co.edu.udistrital.mdp.eventos.repositories.TicketRespository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TicketService {

    @Autowired
    private TicketRespository ticketRepository;

    @Autowired
    private EventRespository eventRepository;

    @Transactional
    public TicketEntity createTicket(TicketEntity ticket) throws EntityNotFoundException, IllegalArgumentException {
        log.info("Iniciando creación de ticket");

        // Validaciones básicas
        if (ticket.getPrice() == null || ticket.getPrice() <= 0) {
            throw new IllegalArgumentException("El precio del ticket debe ser mayor a cero");
        }
        if (ticket.getRemaining() == null || ticket.getRemaining() < 0) {
            throw new IllegalArgumentException("La cantidad disponible no puede ser negativa");
        }
        if (ticket.getClassification() == null || ticket.getClassification().isBlank()) {
            throw new IllegalArgumentException("La clasificación del ticket es obligatoria");
        }

        // Validar evento asociado
        if (ticket.getEvent() == null || ticket.getEvent().getId() == null) {
            throw new IllegalArgumentException("El ticket debe estar asociado a un evento");
        }

        EventEntity event = eventRepository.findById(ticket.getEvent().getId())
            .orElseThrow(() -> new EntityNotFoundException("Evento asociado no encontrado"));
        ticket.setEvent(event);

        log.info("Ticket creado exitosamente con ID: {}", ticket.getId());
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketEntity> getAllTickets() {
        log.info("Obteniendo todos los tickets");
        return ticketRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TicketEntity getTicketById(Long ticketId) throws EntityNotFoundException {
        log.info("Buscando ticket con ID: {}", ticketId);
        return ticketRepository.findById(ticketId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TICKET_NOT_FOUND));
    }

    @Transactional
    public TicketEntity updateTicket(Long ticketId, TicketEntity ticketDetails) 
        throws EntityNotFoundException, IllegalArgumentException {
        
        log.info("Actualizando ticket con ID: {}", ticketId);
        TicketEntity ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TICKET_NOT_FOUND));

        // Validaciones y actualización de campos
        if (ticketDetails.getPrice() != null) {
            if (ticketDetails.getPrice() <= 0) {
                throw new IllegalArgumentException("El precio debe ser mayor a cero");
            }
            ticket.setPrice(ticketDetails.getPrice());
        }

        if (ticketDetails.getRemaining() != null) {
            if (ticketDetails.getRemaining() < 0) {
                throw new IllegalArgumentException("La cantidad disponible no puede ser negativa");
            }
            ticket.setRemaining(ticketDetails.getRemaining());
        }

        if (ticketDetails.getClassification() != null && !ticketDetails.getClassification().isBlank()) {
            ticket.setClassification(ticketDetails.getClassification());
        }

        // Actualización de evento (si se proporciona)
        if (ticketDetails.getEvent() != null && ticketDetails.getEvent().getId() != null) {
            EventEntity event = eventRepository.findById(ticketDetails.getEvent().getId())
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
            ticket.setEvent(event);
        }

        return ticketRepository.save(ticket);
    }

    @Transactional
    public void deleteTicket(Long ticketId) throws EntityNotFoundException {
        log.info("Eliminando ticket con ID: {}", ticketId);
        if (!ticketRepository.existsById(ticketId)) {
            throw new EntityNotFoundException(ErrorMessage.TICKET_NOT_FOUND);
        }
        ticketRepository.deleteById(ticketId);
    }

    @Transactional(readOnly = true)
    public List<TicketEntity> getTicketsByEvent(Long eventId) {
        log.info("Obteniendo tickets para el evento con ID: {}", eventId);
        return ticketRepository.findByEventId(eventId);
    }

    @Transactional(readOnly = true)
    public List<TicketEntity> getTicketsByClassification(String classification) {
        log.info("Buscando tickets por clasificación: {}", classification);
        return ticketRepository.findByClassificationContainingIgnoreCase(classification);
    }

    @Transactional
    public TicketEntity updateTicketAvailability(Long ticketId, Integer quantityChange) 
        throws EntityNotFoundException, IllegalArgumentException {
        
        TicketEntity ticket = getTicketById(ticketId);
        int newQuantity = ticket.getRemaining() + quantityChange;
        
        if (newQuantity < 0) {
            throw new IllegalArgumentException("No hay suficientes tickets disponibles");
        }
        
        ticket.setRemaining(newQuantity);
        return ticketRepository.save(ticket);
    }
}