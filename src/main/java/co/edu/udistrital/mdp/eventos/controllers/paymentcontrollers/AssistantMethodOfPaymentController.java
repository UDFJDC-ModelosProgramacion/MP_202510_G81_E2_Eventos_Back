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
import org.springframework.web.bind.annotation.ResponseStatus;

import co.edu.udistrital.mdp.eventos.dto.paymentdto.MethodOfPaymentDTO;
import co.edu.udistrital.mdp.eventos.entities.paymententity.MethodOfPaymentEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.paymententity.AssistantMethodOfPaymentService;

public class AssistantMethodOfPaymentController {
    @Autowired
    private AssistantMethodOfPaymentService assistantMethodOfPaymentService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value="/assistants/{assistantId}/methodsOfPayment/{methodOfPaymentId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MethodOfPaymentDTO addMethodOfPayment(@PathVariable Long assistantId , @PathVariable Long paymentId)throws EntityNotFoundException {

        MethodOfPaymentEntity methodOfPayment = assistantMethodOfPaymentService.addMethodOfPayment(assistantId, paymentId);
        return modelMapper.map(methodOfPayment, MethodOfPaymentDTO.class);
    }

    @GetMapping(value="/assistants/{assistantId}/methodsOfPayment/{methodOfPaymentId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MethodOfPaymentDTO getMethodOfPayment(@PathVariable Long assistantId,@PathVariable Long paymentId) throws EntityNotFoundException {

        MethodOfPaymentEntity entity = assistantMethodOfPaymentService.getMethodOfPaymentById(assistantId,paymentId);
        return modelMapper.map(entity, MethodOfPaymentDTO.class);
    }

    @GetMapping(value = "/assistants/{assistantId}/methodsOfPayment")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MethodOfPaymentDTO> getMethodsOfPayment(@PathVariable Long assistantId) throws EntityNotFoundException {
        List<MethodOfPaymentEntity> paymentEntities = assistantMethodOfPaymentService.getAllMethodsOfPayment(assistantId);
        return modelMapper.map(paymentEntities, new TypeToken<List<MethodOfPaymentDTO>>() {}.getType());
    }

    @DeleteMapping(value="/assistants/{assistantId}/methodsOfPayment/{methodOfPaymentId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void dissociateMethodOfPayment(@PathVariable Long assistantId,@PathVariable Long paymentId) throws EntityNotFoundException  {
        assistantMethodOfPaymentService.removeMethodOfPayment(assistantId,paymentId);
    }
}
