package co.edu.udistrital.mdp.eventos.services.paymententity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.eventos.entities.paymententity.MobileWalletEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.repositories.AssistantRepository;
import co.edu.udistrital.mdp.eventos.repositories.MethodOfPaymentRepository;
import co.edu.udistrital.mdp.eventos.repositories.MobileWalletRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

@Slf4j
@Service
public class MobileWalletService {
    @Autowired
    MobileWalletRepository mobileWalletRepository;

    @Autowired
    MethodOfPaymentRepository methodOfPaymentRepository;

    @Autowired
    AssistantRepository assistantRepository;

    @Transactional
    public MobileWalletEntity createMobileWallet(MobileWalletEntity mobileWalletEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación de la billetera movil");

        if (mobileWalletRepository.existsByTypeOfWalletAndPhoneAccount(mobileWalletEntity.getTypeOfWallet(),
                mobileWalletEntity.getPhoneAccount())) {
            throw new IllegalOperationException("This mobile wallet is already registered");
        }

        // Guardar primero en creditCardRepository
        MobileWalletEntity savedCard = mobileWalletRepository.save(mobileWalletEntity);

        // También guardar como método de pago (aunque ya esté persistido, puedes hacer un save de nuevo)
        methodOfPaymentRepository.save(savedCard);

        log.info("Termina proceso de creación de la tarjeta de credito");
        return savedCard;
    }

    @Transactional
    public List<MobileWalletEntity> getMobileWallets() {
        log.info("Inicia proceso de consultar todas las billeteras moviles");

        return mobileWalletRepository.findAll();
    }

    @Transactional
    public MobileWalletEntity getMobileWallet(Long mobileWalletId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la billetera movil con id = {0}", mobileWalletId);

        Optional<MobileWalletEntity> mobileWalletEntity = mobileWalletRepository.findById(mobileWalletId);
        if (mobileWalletEntity.isEmpty()) {
            throw new EntityNotFoundException("The mobile wallet does not exist");
        }

        log.info("Inicia proceso de consultar la billetera movil con id = {0}", mobileWalletId);
        return mobileWalletEntity.get();
    }

    @Transactional
    public MobileWalletEntity updateMobileWallet(Long mobileWalletId, MobileWalletEntity mobileWallet)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la billetera movil con id = {0}", mobileWalletId);

        Optional<MobileWalletEntity> mobileWalletEntity = mobileWalletRepository.findById(mobileWalletId);
        if (mobileWalletEntity.isEmpty()) {
            throw new EntityNotFoundException("The mobile wallet does not exist");
        }

        if (mobileWalletRepository.existsByTypeOfWalletAndPhoneAccount(mobileWallet.getTypeOfWallet(),
                mobileWallet.getPhoneAccount())) {
            throw new IllegalOperationException("This mobile wallet is already registered");
        }

        mobileWallet.setId(mobileWalletId);
        return mobileWalletRepository.save(mobileWallet);
    }

    @Transactional
    public void deleteMobileWallet(Long mobileWalletId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de eliminar la billetera movil con id = {0}", mobileWalletId);

        Optional<MobileWalletEntity> mobileWalletEntity = mobileWalletRepository.findById(mobileWalletId);
        if (mobileWalletEntity.isEmpty()) {
            throw new EntityNotFoundException("The mobile wallet does not exist");
        }

        mobileWalletRepository.deleteById(mobileWalletId);
    }
}
