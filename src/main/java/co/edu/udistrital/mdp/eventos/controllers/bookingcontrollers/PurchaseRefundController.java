package co.edu.udistrital.mdp.eventos.controllers.bookingcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.RefundEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.bookingentity.PurchaseRefundService;

@RestController
@RequestMapping("/purchases/{purchaseId}/refund")
public class PurchaseRefundController {

    @Autowired
    private PurchaseRefundService service;

    @PostMapping("/{refundId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RefundEntity addRefund(@PathVariable Long purchaseId, @PathVariable Long refundId) throws EntityNotFoundException {
        return service.addRefund(purchaseId, refundId);
    }

    @GetMapping
    public RefundEntity getRefund(@PathVariable Long purchaseId) throws EntityNotFoundException {
        return service.getRefund(purchaseId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRefund(@PathVariable Long purchaseId) throws EntityNotFoundException {
        service.removeRefund(purchaseId);
    }
}