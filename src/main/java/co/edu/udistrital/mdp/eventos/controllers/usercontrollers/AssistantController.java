package co.edu.udistrital.mdp.eventos.controllers.usercontrollers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.eventos.dto.userdto.AssistantDTO;
import co.edu.udistrital.mdp.eventos.dto.userdto.AssistantDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.userentity.AssistantService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
@RequestMapping("/assistants")
public class AssistantController {

    @Autowired
    private AssistantService assistantService;

    @Autowired
    private ModelMapper modelMapper;

    /*
     * Metodo FindAll para obtener todos los asistentes
     * Utiliza verbo GET
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AssistantDetailDTO> findAll() {
        //List<AssistantEntity> assistants = assistantService.getAllAssistants();
        //return modelMapper.map(assistants, new TypeToken<List<AssistantDetailDTO>>() {
        //}.getType());
        List<AssistantDetailDTO> assistants = assistantService.getAllAssistantsWithPayments();
        return modelMapper.map(assistants, new TypeToken<List<AssistantDetailDTO>>(){}.getType());
    }
    
    /*
     * Metodo FindOne para obtener un asistente por su id
     * Utiliza verbo GET
     */
    //@GetMapping(value = "/{id}")
    //@ResponseStatus(code = HttpStatus.OK)
    //public AssistantDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
    //    AssistantEntity assistantEntity = assistantService.getAssistantById(id);
    //    return modelMapper.map(assistantEntity, AssistantDetailDTO.class);
    //}
    @GetMapping("/{id}")
    public AssistantDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        return assistantService.getAssistantWithPayments(id);
    }

    /*
     * Metodo FindOneByEmail para obtener un asistente por su id
     * Utiliza verbo GET
     */
 
    @GetMapping("/email/{email}")
    @ResponseStatus(code = HttpStatus.OK)
    public AssistantDetailDTO findOneByEmail(@PathVariable String email) throws EntityNotFoundException {
        AssistantEntity assistantEntity = assistantService.getAssistantByEmail(email);
        return modelMapper.map(assistantEntity, AssistantDetailDTO.class);
    }


    /*
     * Metodo Create para crear un asistente
     * Utiliza verbo POST
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AssistantDTO create(@RequestBody AssistantDTO assistantDTO) throws IllegalOperationException, EntityNotFoundException {
        AssistantEntity assistantEntity = assistantService.createAssistant(modelMapper.map(assistantDTO, AssistantEntity.class));
        return modelMapper.map(assistantEntity, AssistantDTO.class);
    }

    /*
     * Metodo Update para actualizar un asistente
     * Utiliza verbo PUT
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AssistantDTO update(@PathVariable Long id, @RequestBody AssistantDTO assistantDTO) throws EntityNotFoundException, IllegalOperationException {
        AssistantEntity assistantEntity = assistantService.updateAssistant(id, modelMapper.map(assistantDTO, AssistantEntity.class));
        return modelMapper.map(assistantEntity, AssistantDTO.class);
    }

    /*
     * Metodo Delete para eliminar un asistente
     * Utiliza verbo DELETE
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        assistantService.deleteAssistant(id);
    }
}
