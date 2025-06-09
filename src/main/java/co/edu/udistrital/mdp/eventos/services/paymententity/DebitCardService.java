package co.edu.udistrital.mdp.eventos.services.paymententity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.eventos.entities.paymententity.DebitCardEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.repositories.DebitCardRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

@Slf4j
@Service
public class DebitCardService {
    @Autowired
    DebitCardRepository debitCardRepository;

    @Transactional
    public DebitCardEntity createDebitCard(DebitCardEntity debitCardEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación de la tarjeta de debito");

        if(debitCardRepository.existsByCardNumber(debitCardEntity.getCardNumber())){
            throw new IllegalOperationException("This debit card is already registered");
        }

        log.info("Termina proceso de creación de la tarjeta de debito");

        return debitCardRepository.save(debitCardEntity);
    }

    @Transactional
    public List<DebitCardEntity> getDebitCards() {
        log.info("Inicia proceso de consultar todas las tarjetas de debito");

        return debitCardRepository.findAll();
    }

    @Transactional
    public DebitCardEntity getDebitCard(Long debitCardId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la tarjeta de debito con id = {0}", debitCardId);

        Optional<DebitCardEntity> debitCardEntity = debitCardRepository.findById(debitCardId);
        if (debitCardEntity.isEmpty()) {
            throw new EntityNotFoundException("The debit card does not exist");
        }

        log.info("Termina proceso de consultar la tarjeta de debito con id = {0}", debitCardId);
        return debitCardEntity.get();
    }

    @Transactional
    public DebitCardEntity updateDebitCard(Long debitCardId, DebitCardEntity debitCard)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la tarjeta de debito con id = {0}", debitCardId);

        Optional<DebitCardEntity> debitCardEntity = debitCardRepository.findById(debitCardId);
        if (debitCardEntity.isEmpty()) {
            throw new EntityNotFoundException("The debit card does not exist");
        }

        debitCard.setId(debitCardId);
        return debitCardRepository.save(debitCard);
    }

    @Transactional
    public void deleteDebitCard(Long debitCardId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de eliminar la tarjeta de debito con id = {0}", debitCardId);

        Optional<DebitCardEntity> debitCardEntity = debitCardRepository.findById(debitCardId);
        if (debitCardEntity.isEmpty()) {
            throw new EntityNotFoundException("The debit card does not exist");
        }

        debitCardRepository.deleteById(debitCardId);
    }

}
