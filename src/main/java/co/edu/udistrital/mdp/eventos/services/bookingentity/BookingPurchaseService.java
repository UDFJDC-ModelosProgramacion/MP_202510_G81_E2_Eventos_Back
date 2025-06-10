package co.edu.udistrital.mdp.eventos.services.bookingentity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.BookingEntity;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.repositories.BookingRepository;
import co.edu.udistrital.mdp.eventos.repositories.PurchaseRepository;

@Service
public class BookingPurchaseService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Transactional
    public PurchaseEntity addPurchase(Long bookingId, Long purchaseId) throws EntityNotFoundException {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.BOOKING_NOT_FOUND));

        PurchaseEntity purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PURCHASE_NOT_FOUND));

        booking.setPurchase(purchase);
        bookingRepository.save(booking);
        return purchase;
    }

    @Transactional
    public PurchaseEntity getPurchase(Long bookingId) throws EntityNotFoundException {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.BOOKING_NOT_FOUND));

        if (booking.getPurchase() == null)
            throw new EntityNotFoundException(ErrorMessage.PURCHASE_NOT_FOUND);

        return booking.getPurchase();
    }

    @Transactional
    public void removePurchase(Long bookingId) throws EntityNotFoundException {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.BOOKING_NOT_FOUND));

        booking.setPurchase(null);
        bookingRepository.save(booking);
    }
}
