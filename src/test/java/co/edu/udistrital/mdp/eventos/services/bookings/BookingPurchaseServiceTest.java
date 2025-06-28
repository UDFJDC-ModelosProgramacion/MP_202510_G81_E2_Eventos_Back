package co.edu.udistrital.mdp.eventos.services.bookings;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.repositories.BookingRepository;
import co.edu.udistrital.mdp.eventos.repositories.PurchaseRepository;
import co.edu.udistrital.mdp.eventos.services.bookingentity.BookingPurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BookingPurchaseServiceTest {

    @Autowired
    private BookingPurchaseService bookingPurchaseService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    private BookingEntity booking;
    private PurchaseEntity purchase;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        purchaseRepository.deleteAll();

        booking = new BookingEntity();
        booking.setRemainingSeats(10);
        booking = bookingRepository.save(booking);

        purchase = new PurchaseEntity();
        purchase.setRemainingSeats(5);
        purchase = purchaseRepository.save(purchase);
    }

    @Test
    @DisplayName("Debe asociar una compra a una reserva")
    void addPurchaseTest() throws EntityNotFoundException {
        PurchaseEntity result = bookingPurchaseService.addPurchase(booking.getId(), purchase.getId());
        assertNotNull(result);
        assertEquals(purchase.getId(), result.getId());
    }

    @Test
    @DisplayName("Debe obtener la compra de una reserva")
    void getPurchaseTest() throws EntityNotFoundException {
        bookingPurchaseService.addPurchase(booking.getId(), purchase.getId());
        PurchaseEntity found = bookingPurchaseService.getPurchase(booking.getId());
        assertNotNull(found);
    }

    @Test
    @DisplayName("Debe eliminar la compra de una reserva")
    void removePurchaseTest() throws EntityNotFoundException {
        bookingPurchaseService.addPurchase(booking.getId(), purchase.getId());
        bookingPurchaseService.removePurchase(booking.getId());
        assertThrows(EntityNotFoundException.class, () -> bookingPurchaseService.getPurchase(booking.getId()));
    }
}
