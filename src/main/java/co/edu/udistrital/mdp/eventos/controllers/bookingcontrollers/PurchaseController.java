package co.edu.udistrital.mdp.eventos.controllers.bookingcontrollers;

import co.edu.udistrital.mdp.eventos.dto.bookingdto.PurchaseDTO;
import co.edu.udistrital.mdp.eventos.dto.bookingdto.PurchaseDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.services.bookingentity.PurchaseService;
import co.edu.udistrital.mdp.eventos.exceptions.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PurchaseDetailDTO> findAll() {
        List<PurchaseEntity> entities = purchaseService.getPurchases();
        return modelMapper.map(entities, new TypeToken<List<PurchaseDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PurchaseDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        PurchaseEntity entity = purchaseService.getPurchase(id);
        return modelMapper.map(entity, PurchaseDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseDTO create(@RequestBody PurchaseDTO dto) throws IllegalOperationException {
        PurchaseEntity entity = purchaseService.createPurchase(modelMapper.map(dto, PurchaseEntity.class));
        return modelMapper.map(entity, PurchaseDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PurchaseDTO update(@PathVariable Long id, @RequestBody PurchaseDTO dto) throws EntityNotFoundException {
        PurchaseEntity entity = purchaseService.updatePurchase(id, modelMapper.map(dto, PurchaseEntity.class));
        return modelMapper.map(entity, PurchaseDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        purchaseService.deletePurchase(id);
    }
}