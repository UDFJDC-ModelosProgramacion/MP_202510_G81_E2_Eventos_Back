package co.edu.udistrital.mdp.eventos.services.paymententity;

import co.edu.udistrital.mdp.eventos.dto.paymentdto.CreditCardDTO;
import co.edu.udistrital.mdp.eventos.dto.paymentdto.DebitCardDTO;
import co.edu.udistrital.mdp.eventos.dto.paymentdto.MethodOfPaymentDTO;
import co.edu.udistrital.mdp.eventos.dto.paymentdto.MobileWalletDTO;
import co.edu.udistrital.mdp.eventos.entities.paymententity.CreditCardEntity;
import co.edu.udistrital.mdp.eventos.entities.paymententity.DebitCardEntity;
import co.edu.udistrital.mdp.eventos.entities.paymententity.MobileWalletEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.repositories.CreditCardRepository;
import co.edu.udistrital.mdp.eventos.repositories.DebitCardRepository;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MethodOfPaymentService {
    
    @Autowired
    private MobileWalletService mobileWalletService;
    
    @Autowired
    private CreditCardRepository creditCardRepository;
    
    @Autowired
    private DebitCardRepository debitCardRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Transactional
    public MethodOfPaymentDTO createMethodOfPayment(MethodOfPaymentDTO paymentDTO) 
            throws IllegalOperationException, EntityNotFoundException {
        
        log.info("Creating method of payment of type: {}", paymentDTO.getType());
        
        switch(paymentDTO.getType().toUpperCase()) {
            case "WALLET":
                MobileWalletDTO walletDTO = modelMapper.map(paymentDTO, MobileWalletDTO.class);
                MobileWalletEntity walletEntity = mobileWalletService.createMobileWallet(
                    modelMapper.map(walletDTO, MobileWalletEntity.class));
                return modelMapper.map(walletEntity, MobileWalletDTO.class);
                
            case "CREDIT_CARD":
                CreditCardDTO creditCardDTO = modelMapper.map(paymentDTO, CreditCardDTO.class);
                CreditCardEntity creditCardEntity = modelMapper.map(creditCardDTO, CreditCardEntity.class);
                creditCardEntity = creditCardRepository.save(creditCardEntity);
                return modelMapper.map(creditCardEntity, CreditCardDTO.class);
                
            case "DEBIT_CARD":
                DebitCardDTO debitCardDTO = modelMapper.map(paymentDTO, DebitCardDTO.class);
                DebitCardEntity debitCardEntity = modelMapper.map(debitCardDTO, DebitCardEntity.class);
                debitCardEntity = debitCardRepository.save(debitCardEntity);
                return modelMapper.map(debitCardEntity, DebitCardDTO.class);
                
            default:
                throw new IllegalOperationException("Unsupported payment method type: " + paymentDTO.getType());
        }
    }
}