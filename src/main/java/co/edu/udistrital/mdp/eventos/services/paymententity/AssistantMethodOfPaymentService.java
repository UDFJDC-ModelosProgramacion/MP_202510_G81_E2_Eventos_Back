package co.edu.udistrital.mdp.eventos.services.paymententity;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.eventos.entities.paymententity.MethodOfPaymentEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.repositories.AssistantRepository;
import co.edu.udistrital.mdp.eventos.repositories.MethodOfPaymentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssistantMethodOfPaymentService {

    @Autowired
    private AssistantRepository assistantRepository;
    @Autowired
    private MethodOfPaymentRepository methodOfPaymentRepository;

    @Transactional
    public MethodOfPaymentEntity associateExistingPayment(Long assistantId, Long paymentId) 
            throws EntityNotFoundException {
        
        // 1. Verificar que existe el assistant
        AssistantEntity assistant = assistantRepository.findById(assistantId)
                .orElseThrow(() -> new EntityNotFoundException("Assistant not found with id: " + assistantId));

        // 2. Verificar que existe el método de pago
        MethodOfPaymentEntity payment = methodOfPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id: " + paymentId));

        // 3. Verificar que no esté ya asociado
        if (payment.getAssistant() != null && payment.getAssistant().getId().equals(assistantId)) {
            return payment; // Ya está asociado, retornar el existente
        }

        // 4. Establecer relación bidireccional
        payment.setAssistant(assistant);
        assistant.getPaymentMethods().add(payment);

        // 5. Guardar cambios (JPA automáticamente actualiza ambas entidades)
        return methodOfPaymentRepository.save(payment);
    }

    @Transactional
    public MethodOfPaymentEntity getMethodOfPaymentById(Long assistantId, Long paymentId) throws EntityNotFoundException {
        Optional<AssistantEntity> assistantOpt = assistantRepository.findById(assistantId);
        Optional<MethodOfPaymentEntity> paymentOpt = methodOfPaymentRepository.findById(paymentId);

        if (assistantOpt.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        if (paymentOpt.isEmpty()) {
            throw new EntityNotFoundException("The method of payment with the given id was not found");
        }

        MethodOfPaymentEntity payment = paymentOpt.get();

        if (payment.getAssistant() == null || !payment.getAssistant().getId().equals(assistantId)) {
            throw new EntityNotFoundException("The method of payment is not associated to the assistant");
        }

        return payment;
    }

    @Transactional
    public List<MethodOfPaymentEntity> getAllMethodsOfPayment(Long assistantId)throws EntityNotFoundException {
        Optional<AssistantEntity> assistantOpt = assistantRepository.findById(assistantId);
        if (assistantOpt.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        return methodOfPaymentRepository.findByAssistantId(assistantId);
    }

    @Transactional
    public void removeMethodOfPayment(Long assistantId, Long paymentId) throws EntityNotFoundException {

        Optional<AssistantEntity> assistantOpt = assistantRepository.findById(assistantId);
        Optional<MethodOfPaymentEntity> paymentOpt = methodOfPaymentRepository.findById(paymentId);

        if (assistantOpt.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        if (paymentOpt.isEmpty()) {
            throw new EntityNotFoundException("The method of payment with the given id was not found");
        }

        MethodOfPaymentEntity payment = paymentOpt.get();

        if (payment.getAssistant() == null || !payment.getAssistant().getId().equals(assistantId)) {
            throw new EntityNotFoundException("The method of payment is not associated to the assistant");
        }

        payment.setAssistant(null);
        methodOfPaymentRepository.save(payment);
    }
}

