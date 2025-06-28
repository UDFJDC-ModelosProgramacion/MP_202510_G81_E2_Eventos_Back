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

import co.edu.udistrital.mdp.eventos.dto.userdto.OrganizerDTO;
import co.edu.udistrital.mdp.eventos.dto.userdto.OrganizerDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.userentity.OrganizerEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.userentity.OrganizerService;

@RestController
@RequestMapping("/organizers")
public class OrganizerController {

    @Autowired
    private OrganizerService organizerService;

    @Autowired
    private ModelMapper modelMapper;

    /*
     * Metodo FindAll para obtener todos los organizadores
     * Utiliza verbo GET
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrganizerDetailDTO> findAll() {
        List<OrganizerEntity> organizers = organizerService.getAllOrganizers();
        return modelMapper.map(organizers, new TypeToken<List<OrganizerDetailDTO>>() {
        }.getType());
    }
    
    /*
     * Metodo FindOne para obtener un organizador por su id
     * Utiliza verbo GET
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public OrganizerDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        OrganizerEntity organizerEntity = organizerService.getOrganizerById(id);
        return modelMapper.map(organizerEntity, OrganizerDetailDTO.class);
    }
    

    /*
     * Metodo Create para crear un organizador
     * Utiliza verbo POST
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public OrganizerDTO create(@RequestBody OrganizerDTO organizerDTO) throws IllegalOperationException, EntityNotFoundException {
        OrganizerEntity organizerEntity = organizerService.createOrganizer(modelMapper.map(organizerDTO, OrganizerEntity.class));
        return modelMapper.map(organizerEntity, OrganizerDTO.class);
    }

    /*
     * Metodo Update para actualizar un organizador
     * Utiliza verbo PUT
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public OrganizerDTO update(@PathVariable Long id, @RequestBody OrganizerDTO organizerDTO) throws EntityNotFoundException, IllegalOperationException {
        OrganizerEntity organizerEntity = organizerService.updateOrganizer(id, modelMapper.map(organizerDTO, OrganizerEntity.class));
        return modelMapper.map(organizerEntity, OrganizerDTO.class);
    }

    /*
     * Metodo Delete para eliminar un organizador
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        organizerService.deleteOrganizer(id);
    }
}
