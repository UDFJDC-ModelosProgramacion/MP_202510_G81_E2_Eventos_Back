package co.edu.udistrital.mdp.eventos.controllers;

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

import co.edu.udistrital.mdp.eventos.dto.paymentdto.CreditCardDTO;
import co.edu.udistrital.mdp.eventos.dto.paymentdto.MethodOfPaymentDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.paymententity.CreditCardEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.CreditCardService;

@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {
    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<MethodOfPaymentDetailDTO> findAll() {

        List<CreditCardEntity> creditCards = creditCardService.getCreditCards();

        return modelMapper.map(creditCards, new TypeToken<List<MethodOfPaymentDetailDTO>>() {
        }.getType());

    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MethodOfPaymentDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {

        CreditCardEntity creditCardEntity = creditCardService.getCreditCard(id);

        return modelMapper.map(creditCardEntity, MethodOfPaymentDetailDTO.class);

    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CreditCardDTO create(@RequestBody CreditCardDTO creditCardDTO)
            throws IllegalOperationException, EntityNotFoundException {

        CreditCardEntity creditCardEntity = creditCardService
                .createCreditCard(modelMapper.map(creditCardDTO, CreditCardEntity.class));

        return modelMapper.map(creditCardEntity, CreditCardDTO.class);

    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CreditCardDTO update(@PathVariable Long id, @RequestBody CreditCardDTO creditCardDTO)
            throws EntityNotFoundException, IllegalOperationException {

        CreditCardEntity creditCardEntity = creditCardService.updateCreditCard(id,
                modelMapper.map(creditCardDTO, CreditCardEntity.class));

        return modelMapper.map(creditCardEntity, CreditCardDTO.class);

    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        creditCardService.deleteCreditCard(id);
    }
}