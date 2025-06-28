package co.edu.udistrital.mdp.eventos.services.bookings;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.repositories.PurchaseRepository;
import co.edu.udistrital.mdp.eventos.services.bookingentity.PurchaseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PurchaseServiceTest {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PurchaseRepository purchaseRepository;

    private PurchaseEntity samplePurchase;

    @BeforeEach
    void setUp() throws EntityNotFoundException {
        purchaseRepository.deleteAll();

        samplePurchase = new PurchaseEntity();
        samplePurchase.setRemainingSeats(10);
        samplePurchase = purchaseService.createPurchase(samplePurchase);
    }

    @Test
    @DisplayName("Debe crear una compra con datos válidos")
    void createPurchaseValidTest() {
        PurchaseEntity newPurchase = new PurchaseEntity();
        newPurchase.setRemainingSeats(5);
        PurchaseEntity saved = purchaseService.createPurchase(newPurchase);
        assertNotNull(saved.getId());
        assertEquals(5, saved.getRemainingSeats());
    }

    @Test
    @DisplayName("No debe permitir crear una compra con asientos negativos")
    void createPurchaseInvalidTest() {
        PurchaseEntity newPurchase = new PurchaseEntity();
        newPurchase.setRemainingSeats(-2);
        assertThrows(IllegalArgumentException.class, () -> purchaseService.createPurchase(newPurchase));
    }

    @Test
    @DisplayName("Debe obtener correctamente una compra existente")
    void getPurchaseValidTest() throws EntityNotFoundException {
        PurchaseEntity found = purchaseService.getPurchase(samplePurchase.getId());
        assertEquals(samplePurchase.getId(), found.getId());
    }

    @Test
    @DisplayName("Debe lanzar excepción si no se encuentra la compra")
    void getPurchaseNotFoundTest() {
        assertThrows(EntityNotFoundException.class, () -> purchaseService.getPurchase(999L));
    }

    @Test
    @DisplayName("Debe actualizar correctamente una compra existente")
    void updatePurchaseValidTest() throws EntityNotFoundException {
        PurchaseEntity updated = new PurchaseEntity();
        updated.setRemainingSeats(7);
        PurchaseEntity result = purchaseService.updatePurchase(samplePurchase.getId(), updated);
        assertEquals(7, result.getRemainingSeats());
    }

    @Test
    @DisplayName("No debe permitir actualizar una compra con asientos negativos")
    void updatePurchaseInvalidTest() {
        PurchaseEntity updated = new PurchaseEntity();
        updated.setRemainingSeats(-10);
        assertThrows(IllegalArgumentException.class, () ->
                purchaseService.updatePurchase(samplePurchase.getId(), updated));
    }

    @Test
    @DisplayName("Debe eliminar correctamente una compra existente")
    void deletePurchaseValidTest() throws EntityNotFoundException {
        purchaseService.deletePurchase(samplePurchase.getId());
        assertFalse(purchaseRepository.findById(samplePurchase.getId()).isPresent());
    }

    @Test
    @DisplayName("Debe lanzar excepción si se intenta eliminar una compra inexistente")
    void deletePurchaseNotFoundTest() {
        assertThrows(EntityNotFoundException.class, () -> purchaseService.deletePurchase(999L));
    }

    @Test
    @DisplayName("Debe obtener todas las compras registradas")
    void getAllPurchasesTest() {
        List<PurchaseEntity> all = purchaseService.getPurchases();
        assertFalse(all.isEmpty());
    }
}
