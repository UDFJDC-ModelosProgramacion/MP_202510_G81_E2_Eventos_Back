package co.edu.udistrital.mdp.eventos.services.userentity.booking;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
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
    public BookingEntity addBooking(Long assistantId, Long bookingId) throws Exception {
        log.info("Inicia el proceso de asociación de una reserva a un asistente con id = {}", assistantId);

        AssistantEntity assistant = assistantRepository.findById(assistantId)
            .orElseThrow(() -> new Exception("Assistant no encontrado con id: " + assistantId));

        BookingEntity booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new Exception("Booking no encontrado con id: " + bookingId));

        booking.setAssistant(assistant);
        return bookingRepository.save(booking);
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
    public List<BookingEntity> getBookings(Long assistantId) throws Exception {
        log.info("Inicia el proceso de obtención de todas las reservas de un asistente con id = {}", assistantId);

        AssistantEntity assistant = assistantRepository.findById(assistantId)
            .orElseThrow(() -> new Exception("Assistant no encontrado con id: " + assistantId));

        return assistant.getBookings();
    }

    /*
	 * Obtiene una instancia de BookingEntity asociada a una instancia de Assistant
	 *
	 * @param assistantId Identificador de la instancia de Assistant
	 * @param bookingId   Identificador de la instancia de Booking
	 * @return La entidadd de Booking del Assistant
	 */

    @Transactional
    public BookingEntity getBooking(Long assistantId, Long bookingId) throws Exception {
        log.info("Inicia el proceso de obtención de una reserva de un asistente con id = {}", assistantId);

        BookingEntity booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new Exception("Booking no encontrado con id: " + bookingId));

        if (booking.getAssistant() == null || !booking.getAssistant().getId().equals(assistantId)) {
            throw new Exception("La reserva no está asociada al asistente especificado");
        }

        return booking;
    }

    /*
	 * Remplaza las instancias de Booking asociadas a una instancia de Assistant
	 *
	 * @param assistantId Identificador de la instancia de Assistant
	 * @param bookings Colección de instancias de BookingEntity a asociar a instancia
	 * de Assistant
	 * @return Nueva colección de BookingEntity asociada a la instancia de Assistant
	 */

    @Transactional
    public List<BookingEntity> replaceBookings(Long assistantId, List<BookingEntity> bookings) throws Exception {
        log.info("Inicia el proceso de reemplazo de las reservas de un asistente con id = {}", assistantId);

        AssistantEntity assistant = assistantRepository.findById(assistantId)
            .orElseThrow(() -> new Exception("Assistant no encontrado con id: " + assistantId));

        // Limpiar asociaciones actuales
        for (BookingEntity existingBooking : assistant.getBookings()) {
            existingBooking.setAssistant(null);
            bookingRepository.save(existingBooking);
        }

        // Asociar las nuevas reservas
        List<BookingEntity> updatedBookings = new ArrayList<>();
        for (BookingEntity booking : bookings) {
            BookingEntity existingBooking = bookingRepository.findById(booking.getId())
                .orElseThrow(() -> new Exception("Booking no encontrado con id: " + booking.getId()));
            existingBooking.setAssistant(assistant);
            updatedBookings.add(bookingRepository.save(existingBooking));
        }

        return updatedBookings;
    }

    /*
	 * Desasocia un Booking existente de un Assistant existente
	 *
	 * @param assistantId Identificador de la instancia de Assistant
	 * @param bookingId   Identificador de la instancia de Booking
	 */

    @Transactional
    public void removeBooking(Long assistantId, Long bookingId) throws Exception {
        log.info("Inicia el proceso de borrado de una reserva de un asistente con id = {}", assistantId);

        BookingEntity booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new Exception("Booking no encontrado con id: " + bookingId));

        if (booking.getAssistant() == null || !booking.getAssistant().getId().equals(assistantId)) {
            throw new Exception("La reserva no pertenece al asistente indicado");
        }

        booking.setAssistant(null);
        bookingRepository.save(booking);
    }
}
