package co.edu.udistrital.mdp.eventos.services.bookings;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.PromoEntity;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.repositories.PromoRepository;
import co.edu.udistrital.mdp.eventos.repositories.PurchaseRepository;
import co.edu.udistrital.mdp.eventos.services.bookingentity.PurchasePromoService;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PurchasePromoServiceTest {

    @Autowired
    private PurchasePromoService service;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PromoRepository promoRepository;

    private PurchaseEntity purchase;
    private PromoEntity promo;

    @BeforeEach
    void setup() {
        promoRepository.deleteAll();
        purchaseRepository.deleteAll();

        promo = new PromoEntity();
        promo.setDescription("Promo de prueba");
        promo.setCode("PRUEBA2025");
        promo.setDiscount(0.25f);
        promo = promoRepository.save(promo);

        purchase = new PurchaseEntity();
        purchase.setRemainingSeats(3);
        purchase = purchaseRepository.save(purchase);
    }

    @Test
    @DisplayName("Debe asociar una promoción a una compra")
    void addPromoTest() throws EntityNotFoundException {
        PromoEntity result = service.addPromo(purchase.getId(), promo.getId());
        assertEquals(promo.getId(), result.getId());
    }

    @Test
    @DisplayName("Debe obtener todas las promociones de una compra")
    void getPromosTest() throws EntityNotFoundException {
        service.addPromo(purchase.getId(), promo.getId());
        List<PromoEntity> promos = service.getPromos(purchase.getId());
        assertEquals(1, promos.size());
    }

    @Test
    @DisplayName("Debe eliminar una promoción de una compra")
    void removePromoTest() throws EntityNotFoundException {
        service.addPromo(purchase.getId(), promo.getId());
        service.removePromo(purchase.getId(), promo.getId());
        List<PromoEntity> promos = service.getPromos(purchase.getId());
        assertTrue(promos.isEmpty());
    }
}
