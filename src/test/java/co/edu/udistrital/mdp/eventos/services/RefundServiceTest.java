package co.edu.udistrital.mdp.eventos.services;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.RefundEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.bookingentity.RefundService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
@Transactional
public class RefundServiceTest {

    @Autowired
    private RefundService refundService;

    @Test
    public void createRefundCorrectTest() {
        RefundEntity refund = new RefundEntity();
        refund.setDate(new Date());

        RefundEntity result = refundService.createRefund(refund);
        Assertions.assertNotNull(result.getId());
    }

    @Test
    public void createRefundInvalidTest() {
        RefundEntity refund = new RefundEntity();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            refundService.createRefund(refund);
        });
    }

    @Test
    public void updateRefundCorrectTest() throws EntityNotFoundException {
        RefundEntity refund = new RefundEntity();
        refund.setDate(new Date());
        refund = refundService.createRefund(refund);

        refund.setDate(new Date());
        RefundEntity updated = refundService.updateRefund(refund.getId(), refund);
        Assertions.assertNotNull(updated.getDate());
    }

    @Test
    public void updateRefundInvalidTest() throws EntityNotFoundException {
        RefundEntity refund = new RefundEntity();
        refund.setDate(new Date());
        refund = refundService.createRefund(refund);

        refund.setDate(null);

        RefundEntity finalRefund = refund;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            refundService.updateRefund(finalRefund.getId(), finalRefund);
        });
    }

    @Test
    public void getRefundCorrectTest() throws EntityNotFoundException {
        RefundEntity refund = new RefundEntity();
        refund.setDate(new Date());
        refund = refundService.createRefund(refund);

        RefundEntity found = refundService.getRefund(refund.getId());
        Assertions.assertNotNull(found);
    }

    @Test
    public void getRefundNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            refundService.getRefund(999L);
        });
    }

    @Test
    public void deleteRefundCorrectTest() throws EntityNotFoundException {
        RefundEntity refund = new RefundEntity();
        refund.setDate(new Date());
        refund = refundService.createRefund(refund);

        Long refundId = refund.getId();

        refundService.deleteRefund(refundId);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            refundService.getRefund(refundId);
        });
    }

    @Test
    public void deleteRefundNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            refundService.deleteRefund(999L);
        });
    }
}