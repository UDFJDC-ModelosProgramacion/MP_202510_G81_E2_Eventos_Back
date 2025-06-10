package co.edu.udistrital.mdp.eventos.controllers.bookingcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.PromoEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.bookingentity.PurchasePromoService;

@RestController
@RequestMapping("/purchases")
public class PurchasePromoController {

    @Autowired
    private PurchasePromoService service;

    @PostMapping("/{promoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PromoEntity addPromo(@PathVariable Long purchaseId, @PathVariable Long promoId) throws EntityNotFoundException {
        return service.addPromo(purchaseId, promoId);
    }

    @GetMapping
    public List<PromoEntity> getPromos(@PathVariable Long purchaseId) throws EntityNotFoundException {
        return service.getPromos(purchaseId);
    }

    @DeleteMapping("/{promoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePromo(@PathVariable Long purchaseId, @PathVariable Long promoId) throws EntityNotFoundException {
        service.removePromo(purchaseId, promoId);
    }
}
