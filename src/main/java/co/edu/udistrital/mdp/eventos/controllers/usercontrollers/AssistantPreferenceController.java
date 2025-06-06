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
import co.edu.udistrital.mdp.eventos.services.userentity.AssistantPreferenceService;

@RestController
@RequestMapping("/assistants")
public class AssistantPreferenceController {
	
	@Autowired
	private AssistantPreferenceService assistantPreferenceService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Busca y devuelve la preference con el ID recibido en la URL, relativo a un asistente.
	 *
	 * @param assistantId El ID del asistant del cual se busca la preference
	 * @param preferenceId   El ID de la preference que se busca
	 * @return {@link PreferenceDetailDTO} - El preference encontrado en el assistant.
	 */
	@GetMapping(value = "/{assistantId}/preferences/{preferenceId}")
	@ResponseStatus(code = HttpStatus.OK)
	public PreferenceDetailDTO getPreference(@PathVariable Long assistantId, @PathVariable Long preferenceId)
			throws EntityNotFoundException, IllegalOperationException {
		PreferenceEntity preferenceEntity = assistantPreferenceService.getPreference(assistantId, preferenceId);
		return modelMapper.map(preferenceEntity, PreferenceDetailDTO.class);
	}

	/**
	 * Busca y devuelve todos los libros que existen en un autor.
	 *
	 * @param assistantsId El ID del autor del cual se buscan los libros
	 * @return JSONArray {@link PreferenceDetailDTO} - Los libros encontrados en el autor.
	 *         Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{assistantId}/preferences")
	@ResponseStatus(code = HttpStatus.OK)
	public List<PreferenceDetailDTO> getPreferences(@PathVariable Long assistantId) throws EntityNotFoundException {
		List<PreferenceEntity> preferenceEntity = assistantPreferenceService.getPreferences(assistantId);
		return modelMapper.map(preferenceEntity, new TypeToken<List<PreferenceDetailDTO>>() {
		}.getType());
	}

	/**
	 * Asocia un libro existente con un autor existente
	 *
	 * @param assistantId El ID del autor al cual se le va a asociar el libro
	 * @param PreferenceId   El ID del libro que se asocia
	 * @return JSON {@link PreferenceDetailDTO} - El libro asociado.
	 */
	@PostMapping(value = "/{assistantId}/preferences/{preferenceId}")
	@ResponseStatus(code = HttpStatus.OK)
	public PreferenceDetailDTO addPreference(@PathVariable Long assistantId, @PathVariable Long preferenceId)
			throws EntityNotFoundException {
		PreferenceEntity preferenceEntity = assistantPreferenceService.addPreference(assistantId, preferenceId);
		return modelMapper.map(preferenceEntity, PreferenceDetailDTO.class);
	}

	/**
	 * Actualiza la lista de libros de un autor con la lista que se recibe en el
	 * cuerpo
	 *
	 * @param assistantId El ID del autor al cual se le va a asociar el libro
	 * @param Preferences    JSONArray {@link PreferenceDTO} - La lista de libros que se desea
	 *                 guardar.
	 * @return JSONArray {@link PreferenceDetailDTO} - La lista actualizada.
	 */
	@PutMapping(value = "/{assistantId}/preferences")
	@ResponseStatus(code = HttpStatus.OK)
	public List<PreferenceDetailDTO> replacePreferences(@PathVariable Long assistantId, @RequestBody List<PreferenceDTO> preferences)
			throws EntityNotFoundException {
		List<PreferenceEntity> entities = modelMapper.map(preferences, new TypeToken<List<PreferenceEntity>>() {
		}.getType());
		List<PreferenceEntity> preferencesList = assistantPreferenceService.addPreferences(assistantId, entities);
		return modelMapper.map(preferencesList, new TypeToken<List<PreferenceDetailDTO>>() {
		}.getType());

	}

    // Agregar metodos para PUT Asociar preferencias a un asistente
    // Agregar metodos para PUT Asociar preferencias que no existen a un asistant
    // Agregar metodos para DELETE Desasociar preferencias a un asistente


	/**
	 * Elimina la conexión entre el libro y e autor recibidos en la URL.
	 *
	 * @param assistantId El ID del autor al cual se le va a desasociar el libro
	 * @param preferenceId   El ID del libro que se desasocia
	 */
	@DeleteMapping(value = "/{assistantId}/preferences/{preferenceId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removePreference(@PathVariable Long assistantId, @PathVariable Long preferenceId)
			throws EntityNotFoundException {
		assistantPreferenceService.removePreference(assistantId, preferenceId);
	}
}
