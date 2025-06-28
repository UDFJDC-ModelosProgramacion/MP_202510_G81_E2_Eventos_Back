package co.edu.udistrital.mdp.eventos.controllers.paymentcontrollers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.eventos.dto.paymentdto.MethodOfPaymentDTO;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.paymententity.MethodOfPaymentService;

@RestController
@RequestMapping("/payments")
public class MethodOfPaymentController {

    @Autowired
    private MethodOfPaymentService paymentService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public MethodOfPaymentDTO createPaymentMethod(@RequestBody MethodOfPaymentDTO paymentDTO) 
            throws IllegalOperationException, EntityNotFoundException {
        return paymentService.createMethodOfPayment(paymentDTO);
    }
}