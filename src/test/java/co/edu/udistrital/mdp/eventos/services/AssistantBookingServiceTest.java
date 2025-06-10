package co.edu.udistrital.mdp.eventos.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.repositories.AssistantRepository;
import co.edu.udistrital.mdp.eventos.repositories.BookingRepository;
import co.edu.udistrital.mdp.eventos.services.userentity.booking.AssistantBookingService;

@ExtendWith(MockitoExtension.class)
public class AssistantBookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    
    @Mock
    private AssistantRepository assistantRepository;
    
    @InjectMocks
    private AssistantBookingService assistantBookingService;
    
    private AssistantEntity assistant;
    private BookingEntity booking;
    
    @BeforeEach
    public void setUp() {
        assistant = new AssistantEntity();
        assistant.setId(1L);
        assistant.setBookings(new ArrayList<>());
        
        booking = new BookingEntity();
        booking.setId(10L);
    }

    // ---------------------------
    // Tests para addBooking(...)
    // ---------------------------
    
    @Test
    @DisplayName("addBooking: asociación válida de booking a assistant")
    public void testAddBooking_Valid() throws Exception {
        // Simular que el assistant y el booking existen
        when(assistantRepository.findById(1L)).thenReturn(Optional.of(assistant));
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(BookingEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        BookingEntity result = assistantBookingService.addBooking(1L, 10L);
        
        // Se espera que el booking tenga asignado el assistant
        assertNotNull(result.getAssistant());
        assertEquals(1L, result.getAssistant().getId());
        
        verify(assistantRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).findById(10L);
        verify(bookingRepository, times(1)).save(booking);
    }
    
    @Test
    @DisplayName("addBooking: assistant no encontrado")
    public void testAddBooking_AssistantNotFound() {
        when(assistantRepository.findById(1L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(Exception.class, () -> {
            assistantBookingService.addBooking(1L, 10L);
        });
        assertTrue(exception.getMessage().contains("Assistant no encontrado con id: 1"));
        
        verify(assistantRepository, times(1)).findById(1L);
        verify(bookingRepository, never()).findById(any());
    }
    
    @Test
    @DisplayName("addBooking: booking no encontrado")
    public void testAddBooking_BookingNotFound() {
        when(assistantRepository.findById(1L)).thenReturn(Optional.of(assistant));
        when(bookingRepository.findById(10L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(Exception.class, () -> {
            assistantBookingService.addBooking(1L, 10L);
        });
        assertTrue(exception.getMessage().contains("Booking no encontrado con id: 10"));
        
        verify(assistantRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).findById(10L);
    }
    
    // ---------------------------
    // Tests para getBookings(...)
    // ---------------------------
    
    @Test
    @DisplayName("getBookings: retorna lista de bookings asociadas al assistant")
    public void testGetBookings_Valid() throws Exception {
        List<BookingEntity> bookings = new ArrayList<>();
        bookings.add(booking);
        assistant.setBookings(bookings);
        when(assistantRepository.findById(1L)).thenReturn(Optional.of(assistant));
        
        List<BookingEntity> result = assistantBookingService.getBookings(1L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(assistantRepository, times(1)).findById(1L);
    }
    
    @Test
    @DisplayName("getBookings: assistant no encontrado")
    public void testGetBookings_AssistantNotFound() {
        when(assistantRepository.findById(1L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(Exception.class, () -> {
            assistantBookingService.getBookings(1L);
        });
        assertTrue(exception.getMessage().contains("Assistant no encontrado con id: 1"));
        verify(assistantRepository, times(1)).findById(1L);
    }

    // ---------------------------
    // Tests para getBooking(...)
    // ---------------------------
    
    @Test
    @DisplayName("getBooking: retorna booking asociado correctamente")
    public void testGetBooking_Valid() throws Exception {
        // Asociar booking al assistant
        booking.setAssistant(assistant);
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
        
        BookingEntity result = assistantBookingService.getBooking(1L, 10L);
        assertNotNull(result);
        assertEquals(1L, result.getAssistant().getId());
        
        verify(bookingRepository, times(1)).findById(10L);
    }
    
    @Test
    @DisplayName("getBooking: booking no encontrado")
    public void testGetBooking_BookingNotFound() {
        when(bookingRepository.findById(10L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(Exception.class, () -> {
            assistantBookingService.getBooking(1L, 10L);
        });
        assertTrue(exception.getMessage().contains("Booking no encontrado con id: 10"));
        verify(bookingRepository, times(1)).findById(10L);
    }
    
    @Test
    @DisplayName("getBooking: booking sin asociación o asociación inválida")
    public void testGetBooking_BookingNotAssociated() {
        // Caso 1: booking con assistant nulo
        booking.setAssistant(null);
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
        
        Exception exception1 = assertThrows(Exception.class, () -> {
            assistantBookingService.getBooking(1L, 10L);
        });
        assertTrue(exception1.getMessage().contains("La reserva no está asociada al asistente especificado"));
        
        // Caso 2: booking con assistant diferente
        AssistantEntity differentAssistant = new AssistantEntity();
        differentAssistant.setId(2L);
        booking.setAssistant(differentAssistant);
        Exception exception2 = assertThrows(Exception.class, () -> {
            assistantBookingService.getBooking(1L, 10L);
        });
        assertTrue(exception2.getMessage().contains("La reserva no está asociada al asistente especificado"));
        
        verify(bookingRepository, times(2)).findById(10L); // se llamará dos veces en el test
    }
    
    // ---------------------------
    // Tests para replaceBookings(...)
    // ---------------------------
    
    @Test
    @DisplayName("replaceBookings: reemplazo exitoso de bookings")
    public void testReplaceBookings_Valid() throws Exception {
        // Suponga que el assistant ya tiene bookings asociados
        BookingEntity existingBooking = new BookingEntity();
        existingBooking.setId(20L);
        existingBooking.setAssistant(assistant);
        List<BookingEntity> currentBookings = new ArrayList<>();
        currentBookings.add(existingBooking);
        assistant.setBookings(currentBookings);
        
        // Nueva lista de bookings a asociar
        BookingEntity newBooking1 = new BookingEntity();
        newBooking1.setId(10L);
        List<BookingEntity> newBookings = new ArrayList<>();
        newBookings.add(newBooking1);
        
        // Simulación de búsquedas
        when(assistantRepository.findById(1L)).thenReturn(Optional.of(assistant));
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(newBooking1));
        when(bookingRepository.save(any(BookingEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookingRepository.save(existingBooking)).thenAnswer(invocation -> invocation.getArgument(0));
        
        List<BookingEntity> result = assistantBookingService.replaceBookings(1L, newBookings);
        
        // Verificar que la asociación antigua se limpió y la nueva se asignó
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getAssistant().getId());
        
        verify(assistantRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).findById(10L);
        // Se hizo llamada para limpiar el booking anterior y para asociar el nuevo booking
    }
    
    @Test
    @DisplayName("replaceBookings: assistant no encontrado")
    public void testReplaceBookings_AssistantNotFound() {
        when(assistantRepository.findById(1L)).thenReturn(Optional.empty());
        List<BookingEntity> newBookings = new ArrayList<>();
        
        Exception exception = assertThrows(Exception.class, () -> {
            assistantBookingService.replaceBookings(1L, newBookings);
        });
        assertTrue(exception.getMessage().contains("Assistant no encontrado con id: 1"));
        verify(assistantRepository, times(1)).findById(1L);
    }
    
    @Test
    @DisplayName("replaceBookings: booking no encontrado en la lista a asociar")
    public void testReplaceBookings_BookingNotFound() {
        assistant.setBookings(new ArrayList<>()); // Sin bookings previas
        List<BookingEntity> newBookings = new ArrayList<>();
        BookingEntity newBooking1 = new BookingEntity();
        newBooking1.setId(30L);
        newBookings.add(newBooking1);
        
        when(assistantRepository.findById(1L)).thenReturn(Optional.of(assistant));
        when(bookingRepository.findById(30L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(Exception.class, () -> {
            assistantBookingService.replaceBookings(1L, newBookings);
        });
        assertTrue(exception.getMessage().contains("Booking no encontrado con id: 30"));
        verify(assistantRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).findById(30L);
    }
    
    // ---------------------------
    // Tests para removeBooking(...)
    // ---------------------------
    
    @Test
    @DisplayName("removeBooking: desasociación exitosa de booking")
    public void testRemoveBooking_Valid() throws Exception {
        // Booking asociado correctamente
        booking.setAssistant(assistant);
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(BookingEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Al remover, se espera que la asociación en booking pase a null
        assistantBookingService.removeBooking(1L, 10L);
        assertNull(booking.getAssistant());
        
        verify(bookingRepository, times(1)).findById(10L);
        verify(bookingRepository, times(1)).save(booking);
    }
    
    @Test
    @DisplayName("removeBooking: booking no encontrado")
    public void testRemoveBooking_BookingNotFound() {
        when(bookingRepository.findById(10L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(Exception.class, () -> {
            assistantBookingService.removeBooking(1L, 10L);
        });
        assertTrue(exception.getMessage().contains("Booking no encontrado con id: 10"));
        verify(bookingRepository, times(1)).findById(10L);
    }
    
    @Test
    @DisplayName("removeBooking: booking no asociado al assistant indicado")
    public void testRemoveBooking_BookingNotAssociated() {
        // Configurar el booking para que tenga un assistant distinto
        AssistantEntity otherAssistant = new AssistantEntity();
        otherAssistant.setId(2L);
        booking.setAssistant(otherAssistant);
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
        
        Exception exception = assertThrows(Exception.class, () -> {
            assistantBookingService.removeBooking(1L, 10L);
        });
        assertTrue(exception.getMessage().contains("La reserva no pertenece al asistente indicado"));
        verify(bookingRepository, times(1)).findById(10L);
    }
}

