package co.edu.udistrital.mdp.eventos.controllers.bookingcontrollers;

import co.edu.udistrital.mdp.eventos.dto.bookingdto.BookingDTO;
import co.edu.udistrital.mdp.eventos.dto.bookingdto.BookingDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.services.bookingentity.BookingService;
import co.edu.udistrital.mdp.eventos.exceptions.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDetailDTO> findAll() {
        List<BookingEntity> entities = bookingService.getBookings();
        return modelMapper.map(entities, new TypeToken<List<BookingDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        BookingEntity entity = bookingService.getBooking(id);
        return modelMapper.map(entity, BookingDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDTO create(@RequestBody BookingDTO dto) throws IllegalOperationException {
        BookingEntity entity = bookingService.createBooking(modelMapper.map(dto, BookingEntity.class));
        return modelMapper.map(entity, BookingDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDTO update(@PathVariable Long id, @RequestBody BookingDTO dto) throws EntityNotFoundException {
        BookingEntity entity = bookingService.updateBooking(id, modelMapper.map(dto, BookingEntity.class));
        return modelMapper.map(entity, BookingDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        bookingService.deleteBooking(id);
    }
}
