package co.edu.udistrital.mdp.eventos.controllers.bookingcontrollers;

import co.edu.udistrital.mdp.eventos.dto.bookingdto.PromoDTO;
import co.edu.udistrital.mdp.eventos.dto.bookingdto.PromoDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.bookingentity.PromoEntity;
import co.edu.udistrital.mdp.eventos.services.bookingentity.PromoService;
import co.edu.udistrital.mdp.eventos.exceptions.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promos")
public class PromoController {

    @Autowired
    private PromoService promoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PromoDetailDTO> findAll() {
        List<PromoEntity> entities = promoService.getPromos();
        return modelMapper.map(entities, new TypeToken<List<PromoDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PromoDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        PromoEntity entity = promoService.getPromo(id);
        return modelMapper.map(entity, PromoDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PromoDTO create(@RequestBody PromoDTO dto) throws IllegalOperationException {
        PromoEntity entity = promoService.createPromo(modelMapper.map(dto, PromoEntity.class));
        return modelMapper.map(entity, PromoDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PromoDTO update(@PathVariable Long id, @RequestBody PromoDTO dto) throws EntityNotFoundException {
        PromoEntity entity = promoService.updatePromo(id, modelMapper.map(dto, PromoEntity.class));
        return modelMapper.map(entity, PromoDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        promoService.deletePromo(id);
    }
}