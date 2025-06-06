package co.edu.udistrital.mdp.eventos.services.userentity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
public class PreferenceAssistantService {

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private AssistantRepository assistantRepository;

    /*
     * Asocia un Assistant existente a una Preference 
     * 
     * @param preferenceId Identificador del Preference
     * @param assistantId Identificador del Assistant
     * @return Intancia de AssistantEntity que fue asociado a Preference
     */

    @Transactional
    public AssistantEntity addAssistant(Long preferenceId, Long assistantId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociarle un autor al libro con id = {0}", preferenceId);
        Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
        if (assistantEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);

        Optional<PreferenceEntity> preferenceEntity = preferenceRepository.findById(preferenceId);
        if (preferenceEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.PREFERENCE_NOT_FOUND);

        preferenceEntity.get().getAssistants().add(assistantEntity.get());
        log.info("Termina proceso de asociarle un autor al libro con id = {0}", preferenceId);
        return assistantEntity.get();
    }

    /*
	 * Obtiene una colección de instancias de AssistantEntity asociadas a una instancia
	 * de Preference
	 *
	 * @param preferenceId Identificador de la instancia de Preference
	 * @return Colección de instancias de AssistantEntity asociadas a la instancia de
	 *         Preference
	 */

    @Transactional
    public List<AssistantEntity> getAssistants(Long preferenceId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar todos los Assistants con la preferenciaId= {0}", preferenceId);
		Optional<PreferenceEntity> preferenceEntity = preferenceRepository.findById(preferenceId);
		if (preferenceEntity.isEmpty()){
			throw new EntityNotFoundException(ErrorMessage.PREFERENCE_NOT_FOUND);
        }
		log.info("Finaliza proceso de consultar todos los Assistants con la preferenciaId = {0}", preferenceId);
		return preferenceEntity.get().getAssistants();
    }

    /*
	 * Obtiene una instancia de AssistantEntity asociada a una instancia de Preference
	 *
	 * @param preferenceId   Identificador de la instancia de Book
	 * @param assistantId Identificador de la instancia de Author
	 * @return La entidad del Autor asociada al libro
	 */
	@Transactional
	public AssistantEntity getAssistant(Long preferenceId, Long assistantId)
			throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar un assistant con preferenceId = {0}", preferenceId);
		Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
		Optional<PreferenceEntity> preferenceEntity = preferenceRepository.findById(preferenceId);

		if (assistantEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);

		if (preferenceEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PREFERENCE_NOT_FOUND);
		log.info("Termina proceso de consultar un assistant con preferenceId = {0}", preferenceId);
		if (!preferenceEntity.get().getAssistants().contains(assistantEntity.get()))
			throw new IllegalOperationException("The assistant is not associated to the preference");
		
		return assistantEntity.get();
	}

	/*
     * Desasocia un Assistant existente de una Preference existente
	 *
     * @param preferenceId   Identificador de la instancia de Preference
     * @param assistantId Identificador de la instancia de Assistant
	 */
    @Transactional
	public void removeAssistant(Long preferenceId, Long assistantId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un assistant con preferenceId = {0}", preferenceId);
		Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
		Optional<PreferenceEntity> preferenceEntity = preferenceRepository.findById(preferenceId);

		if (assistantEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);

		if (preferenceEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PREFERENCE_NOT_FOUND);

		preferenceEntity.get().getAssistants().remove(assistantEntity.get());

		log.info("Termina proceso de borrar un assistant con preferenceId = {0}", preferenceId);
	}

	@Transactional
	public List<AssistantEntity> replaceAssistants(Long preferenceId, List<AssistantEntity> assistants)
        throws EntityNotFoundException {
        log.info("Inicia proceso de reemplazar asistentes en preference con id = {0}", preferenceId);
        Optional<PreferenceEntity> preferenceEntity = preferenceRepository.findById(preferenceId);
        if (preferenceEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.PREFERENCE_NOT_FOUND);
            
        for (AssistantEntity assistant : assistants) {
            if (!assistantRepository.existsById(assistant.getId())) {
                throw new EntityNotFoundException("Assistant with id " + assistant.getId() + " not found.");
            }
        }
    
        List<AssistantEntity> attachedAssistants = assistants.stream()
                .map(a -> assistantRepository.findById(a.getId()).get())
                .collect(Collectors.toList());
    
        preferenceEntity.get().setAssistants(attachedAssistants);
        log.info("Finaliza proceso de reemplazo de asistentes");
        return attachedAssistants;
    }

}
