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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.eventos.dto.userdto.PreferenceDTO;
import co.edu.udistrital.mdp.eventos.dto.userdto.PreferenceDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.userentity.PreferenceEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.userentity.PreferenceService;

@RestController
@RequestMapping("/preferences")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private ModelMapper modelMapper;

    /*
     * Metodo FindAll para obtener todas las preferencias
     * Utiliza verbo GET
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<PreferenceDetailDTO> findAll() {
        List<PreferenceEntity> preferences = preferenceService.getAllPreference();
        return modelMapper.map(preferences, new TypeToken<List<PreferenceDetailDTO>>() {
        }.getType());
    }
    
    /*
     * Metodo FindOne para obtener una preferencia por su id
     * Utiliza verbo GET
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PreferenceDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        PreferenceEntity preferenceEntity = preferenceService.getPreferenceById(id);
        return modelMapper.map(preferenceEntity, PreferenceDetailDTO.class);
    }
    

    /*
     * Metodo Create para crear una preferencia
     * Utiliza verbo POST
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public PreferenceDTO create(@RequestBody PreferenceDTO preferenceDTO) throws IllegalOperationException, EntityNotFoundException {
        PreferenceEntity preferenceEntity = preferenceService.createPreference(modelMapper.map(preferenceDTO, PreferenceEntity.class));
        return modelMapper.map(preferenceEntity, PreferenceDTO.class);
    }

    /*
     * Metodo Update para actualizar una preferencia
     * Utiliza verbo PUT
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PreferenceDTO update(@PathVariable Long id, @RequestBody PreferenceDTO preferenceDTO) throws EntityNotFoundException, IllegalOperationException {
        PreferenceEntity preferenceEntity = preferenceService.updatePreference(id, modelMapper.map(preferenceDTO, PreferenceEntity.class));
        return modelMapper.map(preferenceEntity, PreferenceDTO.class);
    }

    /*
     * Metodo Delete para eliminar una preferencia
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        preferenceService.deletePreference(id);
    }
}
