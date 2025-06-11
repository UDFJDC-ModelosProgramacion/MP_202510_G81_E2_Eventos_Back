package co.edu.udistrital.mdp.eventos.controllers.paymentcontrollers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.eventos.dto.paymentdto.DebitCardDTO;
import co.edu.udistrital.mdp.eventos.dto.paymentdto.MethodOfPaymentDTO;
import co.edu.udistrital.mdp.eventos.entities.paymententity.DebitCardEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.paymententity.DebitCardService;

@RestController
@RequestMapping("/debit_cards")
public class DebitCardController {
    @Autowired
    private DebitCardService debitCardService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<MethodOfPaymentDTO> findAll() {

        List<DebitCardEntity> debitCards = debitCardService.getDebitCards();

        return modelMapper.map(debitCards, new TypeToken<List<MethodOfPaymentDTO>>() {
        }.getType());

    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MethodOfPaymentDTO findOne(@PathVariable Long id) throws EntityNotFoundException {

        DebitCardEntity debitCardEntity = debitCardService.getDebitCard(id);

        return modelMapper.map(debitCardEntity, MethodOfPaymentDTO.class);

    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public DebitCardDTO create(@RequestBody DebitCardDTO debitCardDTO)
            throws IllegalOperationException, EntityNotFoundException {

        DebitCardEntity debitCardEntity = debitCardService
                .createDebitCard(modelMapper.map(debitCardDTO, DebitCardEntity.class));

        return modelMapper.map(debitCardEntity, DebitCardDTO.class);

    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public DebitCardDTO update(@PathVariable Long id, @RequestBody DebitCardDTO debitCardDTO)
            throws EntityNotFoundException, IllegalOperationException {

        DebitCardEntity debitCardEntity = debitCardService.updateDebitCard(id,
                modelMapper.map(debitCardDTO, DebitCardEntity.class));

        return modelMapper.map(debitCardEntity, DebitCardDTO.class);

    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        debitCardService.deleteDebitCard(id);
    }
}
