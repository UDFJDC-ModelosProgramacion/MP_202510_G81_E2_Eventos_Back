package co.edu.udistrital.mdp.eventos.services.bookingentity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.repositories.PurchaseRepository;
import jakarta.transaction.Transactional;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Transactional
    public PurchaseEntity createPurchase(PurchaseEntity purchase) {
        if (purchase.getRemainingSeats() == null || purchase.getRemainingSeats() <= 0) {
            throw new IllegalArgumentException("The number of purchased seats must be greater than 0");
        }
        return purchaseRepository.save(purchase);
    }

    public List<PurchaseEntity> getPurchases() {
        return purchaseRepository.findAll();
    }

    public PurchaseEntity getPurchase(Long id) throws EntityNotFoundException {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase not found with id " + id));
    }

    @Transactional
    public PurchaseEntity updatePurchase(Long id, PurchaseEntity purchase) throws EntityNotFoundException {
        PurchaseEntity existing = getPurchase(id);
        if (purchase.getRemainingSeats() != null && purchase.getRemainingSeats() <= 0) {
            throw new IllegalArgumentException("The number of purchased seats must be greater than 0");
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
    public void deletePurchase(Long id) throws EntityNotFoundException {
        PurchaseEntity purchase = getPurchase(id);
        purchaseRepository.delete(purchase);
    }
}
