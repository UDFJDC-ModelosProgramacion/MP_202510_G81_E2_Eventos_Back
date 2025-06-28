package co.edu.udistrital.mdp.eventos.services.bookings;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.PromoEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.repositories.PromoRepository;
import co.edu.udistrital.mdp.eventos.services.bookingentity.PromoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PromoServiceTest {

    @Autowired
    private PromoService promoService;

    @Autowired
    private PromoRepository promoRepository;

    private PromoEntity samplePromo;

    @BeforeEach
    void setUp() {
        promoRepository.deleteAll();
        samplePromo = new PromoEntity();
        samplePromo.setDescription("Descuento de verano");
        samplePromo.setCode("VERANO2025");
        samplePromo.setDiscount(0.2f);
        promoRepository.save(samplePromo);
    }

    @Test
    @DisplayName("Debe crear una promoción con datos válidos")
    void createPromoValidTest() {
        PromoEntity promo = new PromoEntity();
        promo.setDescription("Descuento navideño");
        promo.setCode("NAVIDAD2025");
        promo.setDiscount(0.3f);

        PromoEntity saved = promoService.createPromo(promo);

        assertNotNull(saved.getId());
        assertEquals("NAVIDAD2025", saved.getCode());
        assertEquals("Descuento navideño", saved.getDescription());
        assertEquals(0.3f, saved.getDiscount());
    }

    @Test
    @DisplayName("No debe permitir crear una promoción con descuento negativo")
    void createPromoInvalidTest() {
        PromoEntity promo = new PromoEntity();
        promo.setDescription("Descuento inválido");
        promo.setCode("INVALIDO");
        promo.setDiscount(-0.1f);

        assertThrows(IllegalArgumentException.class, () -> promoService.createPromo(promo));
    }

    @Test
    @DisplayName("Debe obtener correctamente una promoción existente")
    void getPromoValidTest() throws EntityNotFoundException {
        PromoEntity found = promoService.getPromo(samplePromo.getId());
        assertEquals(samplePromo.getId(), found.getId());
    }

    @Test
    @DisplayName("Debe lanzar excepción si la promoción no existe")
    void getPromoNotFoundTest() {
        assertThrows(EntityNotFoundException.class, () -> promoService.getPromo(999L));
    }

    @Test
    @DisplayName("Debe actualizar correctamente una promoción existente")
    void updatePromoValidTest() throws EntityNotFoundException {
        PromoEntity updated = new PromoEntity();
        updated.setDescription("Descuento actualizado");
        updated.setCode("ACTUALIZADO2025");
        updated.setDiscount(0.5f);

        PromoEntity result = promoService.updatePromo(samplePromo.getId(), updated);

        assertEquals("Descuento actualizado", result.getDescription());
        assertEquals("ACTUALIZADO2025", result.getCode());
        assertEquals(0.5f, result.getDiscount());
    }

    @Test
    @DisplayName("No debe permitir actualizar una promoción con descuento negativo")
    void updatePromoInvalidTest() {
        PromoEntity updated = new PromoEntity();
        updated.setDiscount(-0.2f);

        assertThrows(IllegalArgumentException.class, () ->
                promoService.updatePromo(samplePromo.getId(), updated));
    }

    @Test
    @DisplayName("Debe eliminar correctamente una promoción existente")
    void deletePromoValidTest() throws EntityNotFoundException {
        promoService.deletePromo(samplePromo.getId());
        assertFalse(promoRepository.findById(samplePromo.getId()).isPresent());
    }

    @Test
    @DisplayName("Debe lanzar excepción si se intenta eliminar una promoción inexistente")
    void deletePromoNotFoundTest() {
        assertThrows(EntityNotFoundException.class, () -> promoService.deletePromo(999L));
    }

    @Test
    @DisplayName("Debe obtener todas las promociones registradas")
    void getAllPromosTest() {
        List<PromoEntity> promos = promoService.getPromos();
        assertFalse(promos.isEmpty());
    }
}
