package co.edu.udistrital.mdp.eventos.services.userentity.booking;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.repositories.AssistantRepository;
import co.edu.udistrital.mdp.eventos.repositories.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssistantBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AssistantRepository assistantRepository;


    /*
     * Asocia un Booking exitente a un Assisant
     * 
     * @param assistantId Identificador de instancia de Assistant
     * @param bookingId Identificador de instancia de Booking
     * @return Intancia de BookingEntity que fue asociada a un Assistant
     */

    @Transactional
    public BookingEntity addBooking(Long assistantId, Long bookingId) throws EntityNotFoundException  {
        log.info("Inicia el proceso de asociación de una reserva a un asistente con id = {}", assistantId);
        Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
        Optional<BookingEntity> bookingEntity = bookingRepository.findById(bookingId);

        if(assistantEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        if(bookingEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.BOOKING_NOT_FOUND);
        }
        
        AssistantEntity assistant = assistantEntity.get();
        BookingEntity booking = bookingEntity.get();

        booking.setAssistant(assistant);
        assistant.getBookings().add(booking);

        return booking;
    }

    /*
	 * Obtiene una colección de instancias de BookingEntity asociadas a una instancia
	 * de Assistant
	 *
	 * @param assistantId Identificador de la instancia de Author
	 * @return Colección de instancias de BookingEntity asociadas a la instancia de
	 * Assistant
	 */
    
    @Transactional
    public List<BookingEntity> getBookings(Long assistantId) throws EntityNotFoundException {
        log.info("Inicia el proceso de obtención de todas las reservas de un asistente con id = {}", assistantId);
        Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);

        if(assistantEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        log.info("Termina el proceso de obtención de todas las reservas de un asistente con id = {}", assistantId);

        return assistantEntity.get().getBookings();
    }

    /*
	 * Obtiene una instancia de BookingEntity asociada a una instancia de Assistant
	 *
	 * @param assistantId Identificador de la instancia de Assistant
	 * @param bookingId   Identificador de la instancia de Booking
	 * @return La entidadd de Booking del Assistant
	 */

    @Transactional
    public BookingEntity getBooking(Long assistantId, Long bookingId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia el proceso de obtención de una reserva de un asistente con id = {}", assistantId);
        Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
        Optional<BookingEntity> bookingEntity = bookingRepository.findById(bookingId);

        if(assistantEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        if(bookingEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.BOOKING_NOT_FOUND);
        }
        log.info("Termina el proceso de obtención de una reserva con id = {0} de un asistente con id = {}", bookingId, assistantId);

        if (bookingEntity.get().getAssistant() == null ||!bookingEntity.get().getAssistant().getId().equals(assistantId)) {
            throw new IllegalOperationException("The booking is not associated to the assistant");
        }

        return bookingEntity.get();
    }

    /*
	 * Desasocia un Booking existente de un Assistant existente
	 *
	 * @param assistantId Identificador de la instancia de Assistant
	 * @param bookingId   Identificador de la instancia de Booking
	 */

    @Transactional
    public void removeBooking(Long assistantId, Long bookingId) throws EntityNotFoundException {
        log.info("Inicia el proceso de borrado de una reserva de un asistente con id = {}", assistantId);

        Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
        Optional<BookingEntity> bookingEntity = bookingRepository.findById(bookingId);

        if (assistantEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        if (bookingEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.BOOKING_NOT_FOUND);
        }

        BookingEntity booking = bookingEntity.get();
        AssistantEntity assistant = assistantEntity.get();

        if (booking.getAssistant() == null || !booking.getAssistant().getId().equals(assistantId)) {
            throw new EntityNotFoundException(ErrorMessage.BOOKING_NOT_ASSOCIATED);
        }

        // Desasociar la reserva del asistente
        assistant.getBookings().remove(booking);
        booking.setAssistant(null);

        bookingRepository.save(booking);
        assistantRepository.save(assistant);
        log.info("Termina el proceso de desasociación de un booking de un asistente con id = {}", assistantId);
    }

}
