package co.edu.udistrital.mdp.eventos.services.userentity;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.PreferenceEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.repositories.AssistantRepository;
import co.edu.udistrital.mdp.eventos.repositories.PreferenceRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssistantPreferenceService {

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private AssistantRepository assistantRepository;

    /*
     * Asocia un Preference exitente a un Assisant
     * 
     * @param assistantId Identificador de instancia de Assistant
     * @param preferenceId Identificador de instancia de Preference
     * @return Intancia de PreferenceEntity que fue asociada a un Assistant
     */

    @Transactional
    public PreferenceEntity addPreference(Long assistantId, Long preferenceId) throws EntityNotFoundException {
        log.info("Inicia el proceso de asociación de una Preference a una Assistant con id = {0}", assistantId);
        Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
        Optional<PreferenceEntity> preferenceEntity = preferenceRepository.findById(preferenceId);

        if(assistantEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        if(preferenceEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.PREFERENCE_NOT_FOUND);
        }

        preferenceEntity.get().getAssistants().add(assistantEntity.get());
        log.info("Termina el profeso de asociarle un Preference a un Assistant co id = {0}", assistantId);

        return preferenceEntity.get();
    }

    /*
	 * Obtiene una colección de instancias de PreferenceEntity asociadas a una instancia
	 * de Assistant
	 *
	 * @param assistantId Identificador de la instancia de Author
	 * @return Colección de instancias de PreferenceEntity asociadas a la instancia de
	 *         Assistant
	 */
	@Transactional
	public List<PreferenceEntity> getPreferences(Long assistantId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todas las preferences con AssistantId = {0}", assistantId);
		Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
		if (assistantEntity.isEmpty()){
			throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
		}
		log.info("Termina proceso de consultar todas las preferences con AssistantId = {0}", assistantId);
		return assistantEntity.get().getPreferences();
	}

	/*
	 * Remplaza las instancias de Preference asociadas a una instancia de Assistant
	 *
	 * @param assistantId Identificador de la instancia de Assistant
	 * @param preferences Colección de instancias de PreferenceEntity a asociar a instancia
	 *                 de Assistant
	 * @return Nueva colección de PreferenceEntity asociada a la instancia de Assistant
	 */
	@Transactional
	public List<PreferenceEntity> replacePreferences(Long assistantId, List<PreferenceEntity> preferences) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar los libros asociados al author con id = {0}", assistantId);
		Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
		if (assistantEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);

		for (PreferenceEntity preference : preferences) {
			Optional<PreferenceEntity> preferenceEntity = preferenceRepository.findById(preference.getId());
			if (preferenceEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.PREFERENCE_NOT_FOUND);

		}
		log.info("Finaliza proceso de reemplazar las preferencias asociados al assistant con id = {0}", assistantId);
		assistantEntity.get().setPreferences(preferences);
		return assistantEntity.get().getPreferences();
	}

    /*
	 * Obtiene una instancia de PreferenceEntity asociada a una instancia de Assistant
	 *
	 * @param assistantId Identificador de la instancia de Assistant
	 * @param preferenceId   Identificador de la instancia de Preference
	 * @return La entidadd de Libro del Assistant
	 */
	@Transactional
	public PreferenceEntity getPreference(Long assistantId, Long preferenceId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar una preference con id = {0} del assistant con id = " + assistantId, preferenceId);
		Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
		Optional<PreferenceEntity> preferenceEntity = preferenceRepository.findById(preferenceId);

		if (assistantEntity.isEmpty()){
			throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
		}
		if (preferenceEntity.isEmpty()){
			throw new EntityNotFoundException(ErrorMessage.PREFERENCE_NOT_FOUND);
		}
		log.info("Termina proceso de consultar una preference con id = {0} del assistant con id = " + assistantId, preferenceId);
		if (!preferenceEntity.get().getAssistants().contains(assistantEntity.get())){
			throw new IllegalOperationException("The preference is not associated to the assistant");
		}
		return preferenceEntity.get();
	}

    /*
	 * Desasocia un Preference existente de un Assistant existente
	 *
	 * @param assistantId Identificador de la instancia de Author
	 * @param preferenceId   Identificador de la instancia de Book
	 */
	@Transactional
	public void removePreference(Long assistantId, Long bookId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar una preference con assistantId = {0}", assistantId);
		Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
		if (assistantEntity.isEmpty()){
			throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
		}
		Optional<PreferenceEntity> preferenceEntity = preferenceRepository.findById(bookId);
		if (preferenceEntity.isEmpty()){
			throw new EntityNotFoundException(ErrorMessage.PREFERENCE_NOT_FOUND);
		}
		preferenceEntity.get().getAssistants().remove(assistantEntity.get());
		assistantEntity.get().getPreferences().remove(preferenceEntity.get());
		log.info("Finaliza proceso de borrar una preference con assistantId = {0}", assistantId);
	}
}
