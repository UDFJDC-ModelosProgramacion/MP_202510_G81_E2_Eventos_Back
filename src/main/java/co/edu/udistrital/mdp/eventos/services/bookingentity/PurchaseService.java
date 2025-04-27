package co.edu.udistrital.mdp.eventos.services.bookingentity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.repositories.PurchaseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

public class PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Transactional
    public PurchaseEntity createPurchase(PurchaseEntity purchase) {
        if (purchase.getRemainingSeats() == null || purchase.getRemainingSeats() < 0) {
            throw new IllegalArgumentException("Remaining seats must be non-negative");
        }
        return purchaseRepository.save(purchase);
    }

    public List<PurchaseEntity> getPurchases() {
        return purchaseRepository.findAll();
    }

    public PurchaseEntity getPurchase(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase not found with id " + id));
    }

    @Transactional
    public PurchaseEntity updatePurchase(Long id, PurchaseEntity purchase) {
        PurchaseEntity existing = getPurchase(id);
        if (purchase.getRemainingSeats() != null && purchase.getRemainingSeats() < 0) {
            throw new IllegalArgumentException("Remaining seats must be non-negative");
        }
        existing.setRemainingSeats(purchase.getRemainingSeats());
        existing.setAssistant(purchase.getAssistant());
        existing.setMethodOfPayment(purchase.getMethodOfPayment());
        existing.setNotification(purchase.getNotification());
        existing.setPromos(purchase.getPromos());
        existing.setRefund(purchase.getRefund());
        return purchaseRepository.save(existing);
    }

    @Transactional
    public void deletePurchase(Long id) {
        PurchaseEntity purchase = getPurchase(id);
        purchaseRepository.delete(purchase);
    }
}


