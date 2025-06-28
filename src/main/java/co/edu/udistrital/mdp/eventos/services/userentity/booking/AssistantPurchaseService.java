package co.edu.udistrital.mdp.eventos.services.userentity.booking;


import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.repositories.AssistantRepository;
import co.edu.udistrital.mdp.eventos.repositories.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AssistantPurchaseService {

    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    /**
     * Asocia una Purchase existente a un Assistant.
     *
     * @param assistantId  Identificador del Assistant.
     * @param purchaseId   Identificador de la Purchase.
     * @return La instancia de PurchaseEntity asociada.
     * @throws EntityNotFoundException Si el assistant o la purchase no existen.
     */
    @Transactional
    public PurchaseEntity addPurchase(Long assistantId, Long purchaseId) throws EntityNotFoundException {
        log.info("Inicia el proceso de asociación de una compra a un asistente con id = {}", assistantId);

        Optional<AssistantEntity> assistantOpt = assistantRepository.findById(assistantId);
        Optional<PurchaseEntity> purchaseOpt = purchaseRepository.findById(purchaseId);

        if (assistantOpt.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        if (purchaseOpt.isEmpty()) {
            throw new EntityNotFoundException("The purchase with the given id was not found");
        }

        PurchaseEntity purchase = purchaseOpt.get();
        AssistantEntity assistant = assistantOpt.get();

        purchase.setAssistant(assistant);
        assistant.getPurchases().add(purchase);

        return purchase;
    }

    /**
     * Obtiene la lista de purchases asociadas a un Assistant.
     *
     * @param assistantId Identificador del Assistant.
     * @return Lista de instancias de PurchaseEntity asociadas.
     * @throws EntityNotFoundException Si el assistant no existe.
     */
    @Transactional
    public List<PurchaseEntity> getPurchases(Long assistantId) throws EntityNotFoundException {
        log.info("Inicia el proceso de obtención de compras de un asistente con id = {}", assistantId);

        Optional<AssistantEntity> assistantOpt = assistantRepository.findById(assistantId);
        if (assistantOpt.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }

        // Se asume que AssistantEntity tiene método getPurchases()
        return assistantOpt.get().getPurchases();
    }

    /**
     * Obtiene una Purchase asociada a un Assistant dado un id.
     *
     * @param assistantId Identificador del Assistant.
     * @param purchaseId  Identificador de la Purchase.
     * @return La PurchaseEntity obtenida.
     * @throws EntityNotFoundException    Si el assistant o la purchase no existen.
     * @throws IllegalOperationException  Si la purchase no está asociada al assistant.
     */
    @Transactional
    public PurchaseEntity getPurchase(Long assistantId, Long purchaseId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia el proceso de obtención de una compra con id = {} de un asistente con id = {}", purchaseId, assistantId);

        Optional<AssistantEntity> assistantOpt = assistantRepository.findById(assistantId);
        Optional<PurchaseEntity> purchaseOpt = purchaseRepository.findById(purchaseId);

        if (assistantOpt.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        if (purchaseOpt.isEmpty()) {
            throw new EntityNotFoundException("The purchase with the given id was not found");
        }

        PurchaseEntity purchase = purchaseOpt.get();
        if (purchase.getAssistant() == null || !purchase.getAssistant().getId().equals(assistantId)) {
            throw new IllegalOperationException("The purchase is not associated to the assistant");
        }

        return purchase;
    }

    /**
     * Desasocia una Purchase de un Assistant.
     *
     * @param assistantId Identificador del Assistant.
     * @param purchaseId  Identificador de la Purchase.
     * @throws EntityNotFoundException Si el assistant o la purchase no existen, o si la compra no está asociada.
     */
    @Transactional
    public void removePurchase(Long assistantId, Long purchaseId) throws EntityNotFoundException {
        log.info("Inicia el proceso de desasociación de una compra de un asistente con id = {}", assistantId);

        Optional<AssistantEntity> assistantOpt = assistantRepository.findById(assistantId);
        Optional<PurchaseEntity> purchaseOpt = purchaseRepository.findById(purchaseId);

        if (assistantOpt.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        if (purchaseOpt.isEmpty()) {
            throw new EntityNotFoundException("The purchase with the given id was not found");
        }

        PurchaseEntity purchase = purchaseOpt.get();
        AssistantEntity assistant = assistantOpt.get();

        if (purchase.getAssistant() == null || !purchase.getAssistant().getId().equals(assistantId)) {
            throw new EntityNotFoundException("The purchase is not associated to the assistant");
        }

        assistant.getPurchases().remove(purchase);
        purchase.setAssistant(null);
        
        purchaseRepository.save(purchase);
        assistantRepository.save(assistant);
        log.info("Termina el proceso de desasociación de una compra de un asistente con id = {}", assistantId);
    }
}
