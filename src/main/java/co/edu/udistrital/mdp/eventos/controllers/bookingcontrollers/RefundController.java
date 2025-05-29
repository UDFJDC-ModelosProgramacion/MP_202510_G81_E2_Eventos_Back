package co.edu.udistrital.mdp.eventos.controllers.bookingcontrollers;

import co.edu.udistrital.mdp.eventos.dto.bookingdto.RefundDTO;
import co.edu.udistrital.mdp.eventos.dto.bookingdto.RefundDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.RefundEntity;
import co.edu.udistrital.mdp.eventos.services.bookingentity.RefundService;
import co.edu.udistrital.mdp.eventos.exceptions.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    @Autowired
    private RefundService refundService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RefundDetailDTO> findAll() {
        List<RefundEntity> entities = refundService.getRefunds();
        return modelMapper.map(entities, new TypeToken<List<RefundDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RefundDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        RefundEntity entity = refundService.getRefund(id);
        return modelMapper.map(entity, RefundDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RefundDTO create(@RequestBody RefundDTO dto) throws IllegalOperationException {
        RefundEntity entity = refundService.createRefund(modelMapper.map(dto, RefundEntity.class));
        return modelMapper.map(entity, RefundDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RefundDTO update(@PathVariable Long id, @RequestBody RefundDTO dto) throws EntityNotFoundException {
        RefundEntity entity = refundService.updateRefund(id, modelMapper.map(dto, RefundEntity.class));
        return modelMapper.map(entity, RefundDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        refundService.deleteRefund(id);
    }
}