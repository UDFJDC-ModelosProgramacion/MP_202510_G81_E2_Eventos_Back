package co.edu.udistrital.mdp.eventos.services.bookingentity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.RefundEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.repositories.PurchaseRepository;
import co.edu.udistrital.mdp.eventos.repositories.RefundRepository;

@Service
public class PurchaseRefundService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private RefundRepository refundRepository;

    @Transactional
public RefundEntity addRefund(Long purchaseId, Long refundId) throws EntityNotFoundException {
    PurchaseEntity purchase = purchaseRepository.findById(purchaseId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PURCHASE_NOT_FOUND));

    RefundEntity refund = refundRepository.findById(refundId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.REFUND_NOT_FOUND));

    purchase.setRefund(refund);
    refund.setPurchase(purchase); 

    purchaseRepository.save(purchase);

    return refund;
}

    @Transactional
    public RefundEntity getRefund(Long purchaseId) throws EntityNotFoundException {
        PurchaseEntity purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PURCHASE_NOT_FOUND));

        if (purchase.getRefund() == null)
            throw new EntityNotFoundException(ErrorMessage.REFUND_NOT_FOUND);

        return purchase.getRefund();
    }

    @Transactional
    public void removeRefund(Long purchaseId) throws EntityNotFoundException {
        PurchaseEntity purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PURCHASE_NOT_FOUND));

        purchase.setRefund(null);
        purchaseRepository.save(purchase);
    }
}