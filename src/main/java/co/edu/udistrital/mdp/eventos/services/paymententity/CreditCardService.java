package co.edu.udistrital.mdp.eventos.services.paymententity;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.eventos.entities.paymententity.CreditCardEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.repositories.CreditCardRepository;
import co.edu.udistrital.mdp.eventos.repositories.MethodOfPaymentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

@Slf4j
@Service
public class CreditCardService {
    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    MethodOfPaymentRepository methodOfPaymentRepository;

    @Transactional
    public CreditCardEntity createCreditCard(CreditCardEntity creditCardEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación de la tarjeta de credito");

        if(creditCardRepository.existsByCardNumber(creditCardEntity.getCardNumber())){
            throw new IllegalOperationException("This credit card is already registered");
        }

        LocalDate expirationDate = creditCardEntity.getExpirationDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate actualDate = LocalDate.now();
        if (expirationDate.isBefore(actualDate)) {
            throw new IllegalOperationException("This credit card is expired");
        }

        // Guardar primero en creditCardRepository
        CreditCardEntity savedCard = creditCardRepository.save(creditCardEntity);

        // También guardar como método de pago (aunque ya esté persistido, puedes hacer un save de nuevo)
        methodOfPaymentRepository.save(savedCard);

        log.info("Termina proceso de creación de la tarjeta de credito");
        return savedCard;
    }

    @Transactional
    public List<CreditCardEntity> getCreditCards() {
        log.info("Inicia proceso de consultar todas las tarjetas de credito");

        return creditCardRepository.findAll();
    }

    @Transactional
    public CreditCardEntity getCreditCard(Long creditCardId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la tarjeta de credito con id = {0}", creditCardId);

        Optional<CreditCardEntity> creditCardEntity = creditCardRepository.findById(creditCardId);
        if (creditCardEntity.isEmpty()) {
            throw new EntityNotFoundException("The credit card does not exist");
        }

        log.info("Termina proceso de consultar la tarjeta de credito con id = {0}", creditCardId);
        return creditCardEntity.get();
    }

    @Transactional
    public CreditCardEntity updateCreditCard(Long creditCardId, CreditCardEntity creditCard)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la tarjeta de credito con id = {0}", creditCardId);

        Optional<CreditCardEntity> creditCardEntity = creditCardRepository.findById(creditCardId);
        if (creditCardEntity.isEmpty()) {
            throw new EntityNotFoundException("The credit card does not exist");
        }

        LocalDate expirationDate = creditCard.getExpirationDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate actualDate = LocalDate.now();
        if (expirationDate.isBefore(actualDate)) {
            throw new IllegalOperationException("This credit card is expired");
        }

        creditCard.setId(creditCardId);
        return creditCardRepository.save(creditCard);
    }

    @Transactional
    public void deleteCreditCard(Long creditCardId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de eliminar la tarjeta de credito con id = {0}", creditCardId);

        Optional<CreditCardEntity> creditCardEntity = creditCardRepository.findById(creditCardId);
        if (creditCardEntity.isEmpty()) {
            throw new EntityNotFoundException("The credit card does not exist");
        }

        creditCardRepository.deleteById(creditCardId);
    }
}
