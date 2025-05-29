package co.edu.udistrital.mdp.eventos.services.bookingentity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.PromoEntity;
import co.edu.udistrital.mdp.eventos.repositories.PromoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PromoService {

    @Autowired
    private PromoRepository promoRepository;

    @Transactional
    public PromoEntity createPromo(PromoEntity promo) {
        if (promo.getDiscount() == null || promo.getDiscount() < 0) {
            throw new IllegalArgumentException("Discount must be non-negative");
        }
        return promoRepository.save(promo);
    }

    public List<PromoEntity> getPromos() {
        return promoRepository.findAll();
    }

    public PromoEntity getPromo(Long id) {
        return promoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Promo not found with id " + id));
    }

    @Transactional
    public PromoEntity updatePromo(Long id, PromoEntity promo) {
        PromoEntity existing = getPromo(id);
        if (promo.getDiscount() != null && promo.getDiscount() < 0) {
            throw new IllegalArgumentException("Discount must be non-negative");
        }
        existing.setDescription(promo.getDescription());
        existing.setCode(promo.getCode());
        existing.setDiscount(promo.getDiscount());
        return promoRepository.save(existing);
    }

    @Transactional
    public void deletePromo(Long id) {
        PromoEntity promo = getPromo(id);
        promoRepository.delete(promo);
    }
}
