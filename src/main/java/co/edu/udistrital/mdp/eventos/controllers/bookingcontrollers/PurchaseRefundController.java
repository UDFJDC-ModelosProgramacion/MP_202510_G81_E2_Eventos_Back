package co.edu.udistrital.mdp.eventos.controllers.bookingcontrollers;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.RefundEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.bookingentity.PurchaseRefundService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchases")
public class PurchaseRefundController {

    @Autowired
    private PurchaseRefundService purchaseRefundService;

    @PostMapping("/{purchaseId}/refunds/{refundId}")
    @ResponseStatus(HttpStatus.OK)
    public RefundEntity addRefund(@PathVariable Long purchaseId, @PathVariable Long refundId) throws EntityNotFoundException {
        return purchaseRefundService.addRefund(purchaseId, refundId);
    }

    @GetMapping("/{purchaseId}/refund")
    @ResponseStatus(HttpStatus.OK)
    public RefundEntity getRefund(@PathVariable Long purchaseId) throws EntityNotFoundException {
        return purchaseRefundService.getRefund(purchaseId);
    }

    @DeleteMapping("/{purchaseId}/refund")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRefund(@PathVariable Long purchaseId) throws EntityNotFoundException {
        purchaseRefundService.removeRefund(purchaseId);
    }
}
