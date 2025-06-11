package co.edu.udistrital.mdp.eventos.controllers.usercontrollers;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.eventos.dto.bookingdto.PurchaseDetailDTO;
import co.edu.udistrital.mdp.eventos.dto.userdto.PreferenceDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.userentity.booking.AssistantPurchaseService;

@RestController
@RequestMapping("/assistants")
public class AssistantPurchaseController {

    @Autowired
    private AssistantPurchaseService assistantPurchaseService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Asocia una Purchase existente con un assistant existente
     *
     * @param assistantId El ID del assistant al cual se le va a asociar la compra
     * @param purchaseId  El ID de la Purchase que se asocia
     * @return JSON {@link PreferenceDetailDTO} - La compra asociada.
     */
    @PostMapping(value = "/{assistantId}/purchases/{purchaseId}")
    @ResponseStatus(code = HttpStatus.OK)
    public PurchaseDetailDTO addPurchase(@PathVariable Long assistantId, @PathVariable Long purchaseId)
            throws EntityNotFoundException {
        PurchaseEntity purchaseEntity = assistantPurchaseService.addPurchase(assistantId, purchaseId);
        return modelMapper.map(purchaseEntity, PurchaseDetailDTO.class);
    }

    /**
     * Busca y devuelve la Purchase con el ID recibido en la URL, relativa a un assistant.
     *
     * @param assistantId El ID del assistant del cual se busca la purchase
     * @param purchaseId  El ID de la Purchase que se busca
     * @return {@link PurchaseDetailDTO} - La Purchase encontrada en el assistant.
     */
    @GetMapping(value = "/{assistantId}/purchases/{purchaseId}")
    @ResponseStatus(code = HttpStatus.OK)
    public PurchaseDetailDTO getPurchase(@PathVariable Long assistantId, @PathVariable Long purchaseId)
            throws EntityNotFoundException, IllegalOperationException {
        PurchaseEntity purchaseEntity = assistantPurchaseService.getPurchase(assistantId, purchaseId);
        return modelMapper.map(purchaseEntity, PurchaseDetailDTO.class);
    }

    /**
     * Busca y devuelve todas las purchases que existen en un assistant.
     *
     * @param assistantId El ID del assistant del cual se buscan las purchases
     * @return {@link PurchaseDetailDTO} - Lista de purchases encontradas en el assistant.
     */
    @GetMapping(value = "/{assistantId}/purchases")
    @ResponseStatus(code = HttpStatus.OK)
    public List<PurchaseDetailDTO> getPurchases(@PathVariable Long assistantId) throws EntityNotFoundException {
        List<PurchaseEntity> purchaseEntities = assistantPurchaseService.getPurchases(assistantId);
        return modelMapper.map(purchaseEntities, new TypeToken<List<PurchaseDetailDTO>>() {}.getType());
    }

    /**
     * Elimina la conexión entre la Purchase y el assistant recibidos en la URL.
     *
     * @param assistantId El ID del assistant al cual se le va a desasociar la Purchase
     * @param purchaseId  El ID de la Purchase que se desasocia
     */
    @DeleteMapping(value = "/{assistantId}/purchases/{purchaseId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removePurchase(@PathVariable Long assistantId, @PathVariable Long purchaseId)
            throws EntityNotFoundException {
        assistantPurchaseService.removePurchase(assistantId, purchaseId);
    }
}
