package co.edu.udistrital.mdp.eventos.controllers.eventcontrollers;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.eventos.dto.eventdto.TicketDTO;
import co.edu.udistrital.mdp.eventos.entities.evententity.TicketEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.evententity.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TicketDTO> findAll() {
        List<TicketEntity> tickets = ticketService.getAllTickets();
        return modelMapper.map(tickets, new TypeToken<List<TicketDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TicketDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        TicketEntity ticketEntity = ticketService.getTicketById(id);
        return modelMapper.map(ticketEntity, TicketDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDTO create(@RequestBody TicketDTO ticketDTO) throws EntityNotFoundException, IllegalArgumentException {
        TicketEntity ticketEntity = ticketService.createTicket(modelMapper.map(ticketDTO, TicketEntity.class));
        return modelMapper.map(ticketEntity, TicketDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TicketDTO update(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) 
            throws EntityNotFoundException, IllegalArgumentException {
        TicketEntity ticketEntity = ticketService.updateTicket(id, modelMapper.map(ticketDTO, TicketEntity.class));
        return modelMapper.map(ticketEntity, TicketDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        ticketService.deleteTicket(id);
    }

    @PatchMapping("/{id}/availability")
    @ResponseStatus(HttpStatus.OK)
    public TicketDTO updateAvailability(@PathVariable Long id, @RequestParam Integer quantity) 
            throws EntityNotFoundException, IllegalArgumentException {
        TicketEntity ticketEntity = ticketService.updateTicketAvailability(id, quantity);
        return modelMapper.map(ticketEntity, TicketDTO.class);
    }
}
