package co.edu.udistrital.mdp.eventos.services.bookingentity;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.PromoEntity;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.repositories.PromoRepository;
import co.edu.udistrital.mdp.eventos.repositories.PurchaseRepository;

@Service
public class PurchasePromoService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Transactional
    public PromoEntity addPromo(Long purchaseId, Long promoId) throws EntityNotFoundException {
        PurchaseEntity purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PURCHASE_NOT_FOUND));

        PromoEntity promo = promoRepository.findById(promoId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PROMO_NOT_FOUND));

        purchase.getPromos().add(promo);
        promo.setPurchase(purchase);
        purchaseRepository.save(purchase);

        return promo;
    }

    @Transactional
    public List<PromoEntity> getPromos(Long purchaseId) throws EntityNotFoundException {
        PurchaseEntity purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PURCHASE_NOT_FOUND));

        return purchase.getPromos();
    }

    @Transactional
    public void removePromo(Long purchaseId, Long promoId) throws EntityNotFoundException {
        PurchaseEntity purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PURCHASE_NOT_FOUND));

        PromoEntity promo = promoRepository.findById(promoId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PROMO_NOT_FOUND));

        if (!purchase.getPromos().contains(promo))
            throw new EntityNotFoundException("Promo not associated with this purchase");

        purchase.getPromos().remove(promo);
        promo.setPurchase(null);
        purchaseRepository.save(purchase);
    }
}