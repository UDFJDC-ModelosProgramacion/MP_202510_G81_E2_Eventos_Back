package co.edu.udistrital.mdp.eventos.services;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.RefundEntity;
import co.edu.udistrital.mdp.eventos.repositories.RefundRepository;
import co.edu.udistrital.mdp.eventos.services.bookingentity.RefundService;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RefundServiceTest {

    @Autowired
    private RefundService refundService;

    @Autowired
    private RefundRepository refundRepository;

    private RefundEntity sampleRefund;

    @BeforeEach
    void setUp() {
        refundRepository.deleteAll();
        sampleRefund = new RefundEntity();
        sampleRefund.setReason("Test reason");
        sampleRefund.setDate(new Date());
        refundRepository.save(sampleRefund);
    }

    @Test
    @DisplayName("Debe crear una devolución con datos válidos")
    void createRefundValidTest() {
        RefundEntity refund = new RefundEntity();
        refund.setReason("Otra razón válida");
        refund.setDate(new Date());
        RefundEntity saved = refundService.createRefund(refund);
        assertNotNull(saved.getId());
        assertEquals("Otra razón válida", saved.getReason());
    }

    @Test
    @DisplayName("No debe permitir crear una devolución con razón vacía")
    void createRefundInvalidTest() {
        RefundEntity refund = new RefundEntity();
        refund.setReason("");  // razón vacía
        refund.setDate(new Date());
        assertThrows(IllegalArgumentException.class, () -> refundService.createRefund(refund));
    }

    @Test
    @DisplayName("Debe obtener correctamente una devolución existente")
    void getRefundValidTest() throws EntityNotFoundException {
        RefundEntity found = refundService.getRefund(sampleRefund.getId());
        assertEquals(sampleRefund.getId(), found.getId());
        assertEquals("Test reason", found.getReason());
    }

    @Test
    @DisplayName("Debe lanzar excepción si la devolución no existe")
    void getRefundNotFoundTest() {
        assertThrows(EntityNotFoundException.class, () -> refundService.getRefund(999L));
    }

    @Test
    @DisplayName("Debe actualizar correctamente una devolución existente")
    void updateRefundValidTest() throws EntityNotFoundException {
        RefundEntity updated = new RefundEntity();
        updated.setReason("Razón actualizada");
        updated.setDate(new Date());
        RefundEntity result = refundService.updateRefund(sampleRefund.getId(), updated);
        assertEquals("Razón actualizada", result.getReason());
    }

    @Test
    @DisplayName("No debe permitir actualizar una devolución con razón vacía")
    void updateRefundInvalidTest() {
        RefundEntity updated = new RefundEntity();
        updated.setReason("");
        updated.setDate(new Date());
        assertThrows(IllegalArgumentException.class, () ->
                refundService.updateRefund(sampleRefund.getId(), updated));
    }

    @Test
    @DisplayName("Debe eliminar correctamente una devolución existente")
    void deleteRefundValidTest() throws EntityNotFoundException {
        refundService.deleteRefund(sampleRefund.getId());
        assertFalse(refundRepository.findById(sampleRefund.getId()).isPresent());
    }

    @Test
    @DisplayName("Debe lanzar excepción si se intenta eliminar una devolución inexistente")
    void deleteRefundNotFoundTest() {
        assertThrows(EntityNotFoundException.class, () -> refundService.deleteRefund(999L));
    }

    @Test
    @DisplayName("Debe obtener todas las devoluciones registradas")
    void getAllRefundsTest() {
        List<RefundEntity> refunds = refundService.getRefunds();
        assertFalse(refunds.isEmpty());
    }
}
