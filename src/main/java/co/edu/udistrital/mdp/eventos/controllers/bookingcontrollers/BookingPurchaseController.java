package co.edu.udistrital.mdp.eventos.controllers.bookingcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.bookingentity.BookingPurchaseService;

@RestController
@RequestMapping("/api/bookings/{bookingId}/purchase")
public class BookingPurchaseController {

    @Autowired
    private BookingPurchaseService service;

    @PostMapping("/{purchaseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseEntity addPurchase(@PathVariable Long bookingId, @PathVariable Long purchaseId) throws EntityNotFoundException {
        return service.addPurchase(bookingId, purchaseId);
    }

    @GetMapping
    public PurchaseEntity getPurchase(@PathVariable Long bookingId) throws EntityNotFoundException {
        return service.getPurchase(bookingId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePurchase(@PathVariable Long bookingId) throws EntityNotFoundException {
        service.removePurchase(bookingId);
    }
}
