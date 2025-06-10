package co.edu.udistrital.mdp.eventos.controllers.usercontrollers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.eventos.dto.bookingdto.BookingDetailDTO;
import co.edu.udistrital.mdp.eventos.dto.userdto.PreferenceDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.userentity.booking.AssistantBookingService;

@RestController
@RequestMapping("/assistants")
public class AssistantBookingController {

    @Autowired
    private AssistantBookingService assistantBookingService;

    @Autowired
    private ModelMapper modelMapper;

    /**
	 * Asocia un booking existente con un assistant existente
	 *
	 * @param assistantId El ID del assitant al cual se le va a asociar el libro
	 * @param BookingId   El ID del booking que se asocia
	 * @return JSON {@link PreferenceDetailDTO} - El libro asociado. 
	 */
	@PostMapping(value = "/{assistantId}/bookings/{bookingId}")
	@ResponseStatus(code = HttpStatus.OK)
	public BookingDetailDTO addBooking(@PathVariable Long assistantId, @PathVariable Long bookingId)
			throws EntityNotFoundException {
		BookingEntity bookingEntity = assistantBookingService.addBooking(assistantId, bookingId);
		return modelMapper.map(bookingEntity, BookingDetailDTO.class);
	}

	/**
	 * Busca y devuelve la booking con el ID recibido en la URL, relativo a un asistente.
	 *
	 * @param assistantId El ID del asistant del cual se busca la preference
	 * @param bookingId   El ID de la booking que se busca
	 * @return {@link BookingDetailDTO} - El booking encontrado en el assistant.
	 */
	@GetMapping(value = "/{assistantId}/bookings/{bookingId}")
	@ResponseStatus(code = HttpStatus.OK)
	public BookingDetailDTO getBooking(@PathVariable Long assistantId, @PathVariable Long bookingId) throws EntityNotFoundException, IllegalOperationException {
		BookingEntity bookingEntity = assistantBookingService.getBooking(assistantId, bookingId);
		return modelMapper.map(bookingEntity, BookingDetailDTO.class);
	}

		/**
	 * Busca y devuelve todos los bookings que existen en un assistant.
	 *
	 * @param assistantId El ID del asistant del cual se busca la preference
	 * @param bookingId   El ID de la booking que se busca
	 * @return {@link BookingDetailDTO} - El booking encontrado en el assistant.
	 */
	@GetMapping(value = "/{assistantId}/bookings")
	@ResponseStatus(code = HttpStatus.OK)
	public List<BookingDetailDTO> getBookings(@PathVariable Long assistantId) throws EntityNotFoundException {
		List<BookingEntity> bookingEntity = assistantBookingService.getBookings(assistantId);
		return modelMapper.map(bookingEntity, new TypeToken<List<BookingDetailDTO>>() {}.getType());
	}

	/**
	 * Elimina la conexión entre el booking y el assistant recibidos en la URL.
	 *
	 * @param assistantId El ID del assistant al cual se le va a desasociar el booking
	 * @param bookingId   El ID del booking que se desasocia
	 */
	@DeleteMapping(value = "/{assistantId}/bookings/{bookingId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeBooking(@PathVariable Long assistantId, @PathVariable Long bookingId)
			throws EntityNotFoundException {
		assistantBookingService.removeBooking(assistantId, bookingId);
	}
}
