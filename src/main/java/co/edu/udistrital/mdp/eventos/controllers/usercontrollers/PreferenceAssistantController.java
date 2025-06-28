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

import co.edu.udistrital.mdp.eventos.dto.userdto.AssistantDTO;
import co.edu.udistrital.mdp.eventos.dto.userdto.AssistantDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.userentity.PreferenceAssistantService;

@RestController
@RequestMapping("/preferences")
public class PreferenceAssistantController {

    @Autowired
    private PreferenceAssistantService preferenceAssistantService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Asocia un asistente existente con una preferencia existente
     *
     * @param assistantId El ID del asistente que se va a asociar
     * @param preferenceId   El ID de la preferencia a la cual se le va a asociar el asistente
     * @return JSON {@link AssistantDetailDTO} - El asistente asociado.
     */
    @PostMapping(value = "/{preferenceId}/assistants/{assistantId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AssistantDetailDTO addAssistant(@PathVariable Long assistantId, @PathVariable Long preferenceId)
            throws EntityNotFoundException {
        AssistantEntity assistantEntity = preferenceAssistantService.addAssistant(preferenceId, assistantId);
        return modelMapper.map(assistantEntity, AssistantDetailDTO.class);
    }

    /**
     * Busca y devuelve el asistente con el ID recibido en la URL, relativo a una preferencia.
     *
     * @param assistantId El ID del asistente que se busca
     * @param preferenceId   El ID de la preferencia de la cual se busca el asistente
     * @return {@link AssistantDetailDTO} - El asistente encontrado en la preferencia.
     */
    @GetMapping(value = "/{preferenceId}/assistants/{assistantId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AssistantDetailDTO getAssistant(@PathVariable Long assistantId, @PathVariable Long preferenceId)
            throws EntityNotFoundException, IllegalOperationException {
        AssistantEntity assistantEntity = preferenceAssistantService.getAssistant(preferenceId, assistantId);
        return modelMapper.map(assistantEntity, AssistantDetailDTO.class);
    }

    /**
     * Actualiza la lista de asistentes de una preferencia con la lista que se recibe en el cuerpo.
     *
     * @param preferenceId  El ID de la preferencia a la cual se le va a asociar la lista de asistentes
     * @param assistants JSONArray {@link AssistantDTO} - La lista de asistentes que se desea guardar.
     * @return JSONArray {@link AssistantDetailDTO} - La lista actualizada.
     */
    @PutMapping(value = "/{preferenceId}/assistants")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AssistantDetailDTO> addAssistants(@PathVariable Long preferenceId, @RequestBody List<AssistantDTO> assistants)
            throws EntityNotFoundException {
        List<AssistantEntity> entities = modelMapper.map(assistants, new TypeToken<List<AssistantEntity>>() {
        }.getType());
        List<AssistantEntity> assistantsList = preferenceAssistantService.replaceAssistants(preferenceId, entities);
        return modelMapper.map(assistantsList, new TypeToken<List<AssistantDetailDTO>>() {
        }.getType());
    }

    /**
     * Busca y devuelve todos los asistentes que existen en una preferencia.
     *
     * @param preferenceId El ID de la preferencia de la cual se buscan los asistentes
     * @return JSONArray {@link AssistantDetailDTO} - Los asistentes encontrados en la preferencia.
     *         Si no hay ninguno retorna una lista vacía.
     */
    @GetMapping(value = "/{preferenceId}/assistants")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AssistantDetailDTO> getAssistants(@PathVariable Long preferenceId) throws EntityNotFoundException {
        List<AssistantEntity> assistantEntity = preferenceAssistantService.getAssistants(preferenceId);
        return modelMapper.map(assistantEntity, new TypeToken<List<AssistantDetailDTO>>() {
        }.getType());
    }

    /**
     * Elimina la conexión entre el asistente y la preferencia recibidos en la URL.
     *
     * @param preferenceId   El ID de la preferencia a la cual se le va a desasociar el asistente
     * @param assistantId El ID del asistente que se desasocia
     */
    @DeleteMapping(value = "/{preferenceId}/assistants/{assistantId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeAssistant(@PathVariable Long assistantId, @PathVariable Long preferenceId)
            throws EntityNotFoundException {
        preferenceAssistantService.removeAssistant(preferenceId, assistantId);
    }
}

