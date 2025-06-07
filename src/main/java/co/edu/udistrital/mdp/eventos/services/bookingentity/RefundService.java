package co.edu.udistrital.mdp.eventos.services.bookingentity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.RefundEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.repositories.RefundRepository;
import jakarta.transaction.Transactional;

@Service
public class RefundService {

    @Autowired
    private RefundRepository refundRepository;

    @Transactional
    public RefundEntity createRefund(RefundEntity refund) {
        if (refund.getDate() == null) {
            throw new IllegalArgumentException("Refund date must be specified");
        }
        if (refund.getReason() == null || refund.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("Refund reason must be specified");
        }
        return refundRepository.save(refund);
    }

    public List<RefundEntity> getRefunds() {
        return refundRepository.findAll();
    }

    public RefundEntity getRefund(Long id) throws EntityNotFoundException {
        return refundRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Refund not found with id " + id));
    }

    @Transactional
    public RefundEntity updateRefund(Long id, RefundEntity refund) throws EntityNotFoundException {
        RefundEntity existing = getRefund(id);

        if (refund.getDate() == null) {
            throw new IllegalArgumentException("Refund date must be specified");
        }
        if (refund.getReason() == null || refund.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("Refund reason must be specified");
        }

        existing.setDate(refund.getDate());
        existing.setReason(refund.getReason());

        return refundRepository.save(existing);
    }

    @Transactional
    public void deleteRefund(Long id) throws EntityNotFoundException {
        RefundEntity refund = getRefund(id);
        refundRepository.delete(refund);
    }
}
