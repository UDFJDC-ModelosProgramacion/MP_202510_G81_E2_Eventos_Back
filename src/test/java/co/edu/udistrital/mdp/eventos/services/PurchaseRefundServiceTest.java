package co.edu.udistrital.mdp.eventos.services;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.RefundEntity;
import co.edu.udistrital.mdp.eventos.repositories.PurchaseRepository;
import co.edu.udistrital.mdp.eventos.repositories.RefundRepository;
import co.edu.udistrital.mdp.eventos.services.bookingentity.PurchaseRefundService;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PurchaseRefundServiceTest {

    @Autowired
    private PurchaseRefundService service;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private RefundRepository refundRepository;

    private PurchaseEntity purchase;
    private RefundEntity refund;

    @BeforeEach
    void setup() {
        refundRepository.deleteAll();
        purchaseRepository.deleteAll();

        refund = new RefundEntity();
        refund.setReason("Test refund");
        refund.setDate(new Date());
        refund = refundRepository.save(refund);

        purchase = new PurchaseEntity();
        purchase.setRemainingSeats(5);
        purchase = purchaseRepository.save(purchase);
    }

    @Test
    @DisplayName("Debe asociar una devolución a una compra")
    void addRefundTest() throws EntityNotFoundException {
        RefundEntity result = service.addRefund(purchase.getId(), refund.getId());
        assertEquals(refund.getId(), result.getId());
    }

    @Test
    @DisplayName("Debe obtener la devolución asociada a una compra")
    void getRefundTest() throws EntityNotFoundException {
        service.addRefund(purchase.getId(), refund.getId());
        RefundEntity result = service.getRefund(purchase.getId());
        assertEquals(refund.getId(), result.getId());
    }

    @Test
    @DisplayName("Debe eliminar la devolución de una compra")
    void removeRefundTest() throws EntityNotFoundException {
        service.addRefund(purchase.getId(), refund.getId());
        service.removeRefund(purchase.getId());
        assertThrows(EntityNotFoundException.class, () -> service.getRefund(purchase.getId()));
    }
}