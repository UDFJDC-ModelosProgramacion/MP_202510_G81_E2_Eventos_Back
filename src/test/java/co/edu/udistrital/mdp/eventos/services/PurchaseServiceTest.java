package co.edu.udistrital.mdp.eventos.services.bookingentity;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class PurchaseServiceTest {

    @Autowired
    private PurchaseService purchaseService;

    @Test
    public void createPurchaseCorrectTest() {
        PurchaseEntity purchase = new PurchaseEntity();
        purchase.setRemainingSeats(7);

        PurchaseEntity result = purchaseService.createPurchase(purchase);
        Assertions.assertNotNull(result.getId());
    }

    @Test
    public void createPurchaseInvalidTest() {
        PurchaseEntity purchase = new PurchaseEntity();
        purchase.setRemainingSeats(-3);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            purchaseService.createPurchase(purchase);
        });
    }

    @Test
    public void updatePurchaseCorrectTest() {
        PurchaseEntity purchase = new PurchaseEntity();
        purchase.setRemainingSeats(4);
        purchase = purchaseService.createPurchase(purchase);

        purchase.setRemainingSeats(9);
        PurchaseEntity updated = purchaseService.updatePurchase(purchase.getId(), purchase);
        Assertions.assertEquals(9, updated.getRemainingSeats());
    }

    @Test
    public void updatePurchaseInvalidTest() {
        PurchaseEntity purchase = new PurchaseEntity();
        purchase.setRemainingSeats(6);
        purchase = purchaseService.createPurchase(purchase);

        purchase.setRemainingSeats(-5);

        PurchaseEntity finalPurchase = purchase;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            purchaseService.updatePurchase(finalPurchase.getId(), finalPurchase);
        });
    }

    @Test
    public void getPurchaseCorrectTest() {
        PurchaseEntity purchase = new PurchaseEntity();
        purchase.setRemainingSeats(5);
        purchase = purchaseService.createPurchase(purchase);

        PurchaseEntity found = purchaseService.getPurchase(purchase.getId());
        Assertions.assertNotNull(found);
    }

    @Test
    public void getPurchaseNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            purchaseService.getPurchase(999L);
        });
    }

    @Test
    public void deletePurchaseCorrectTest() {
        PurchaseEntity purchase = new PurchaseEntity();
        purchase.setRemainingSeats(3);
        purchase = purchaseService.createPurchase(purchase);

        Long purchaseId = purchase.getId();

        purchaseService.deletePurchase(purchaseId);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            purchaseService.getPurchase(purchaseId);
        });
    }

    @Test
    public void deletePurchaseNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            purchaseService.deletePurchase(999L);
        });
    }
}
