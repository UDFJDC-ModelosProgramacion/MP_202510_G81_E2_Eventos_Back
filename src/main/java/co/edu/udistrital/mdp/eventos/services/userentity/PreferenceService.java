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
import co.edu.udistrital.mdp.eventos.repositories.AssistantRepository;
import co.edu.udistrital.mdp.eventos.repositories.PreferenceRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PreferenceService {

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private AssistantRepository assistantRepository;

    /*
     * Se encarga de crear una nueva Preference
     * 
     * @param preference Objeto de PreferenceEntity.
     * @Return Objeto de PreferenceEntity con los datos nuevos y su ID.
     * @throws Exception Si ocurre un error al crear la preferencia.
     */

    @Transactional
    public PreferenceEntity createPreference(PreferenceEntity preference) throws EntityNotFoundException {
        log.info("Inicia el proceso de creación de una preferencia");
    
        if (preference.getDescription() == null || preference.getDescription().isBlank()) {
            throw new IllegalArgumentException("La descripción de la preferencia es obligatoria.");
        }
    
        // Validar asistentes múltiples
        if (preference.getAssistants() != null) {
            for (AssistantEntity assistant : preference.getAssistants()) {
                if (assistant.getId() == null || !assistantRepository.existsById(assistant.getId())) {
                    throw new EntityNotFoundException("Uno de los asistentes asociados no existe.");
                }
            }
        }
    
        log.info("Preferencia creada con id={}", preference.getId());
        return preferenceRepository.save(preference);
    }
    

    /*
	 * Obtiene la lista de los registros de Preference.
	 *
	 * @return Colección de objetos de PreferenceEntity.
	 */

    @Transactional
    public List<PreferenceEntity> getAllPreference() {
        log.info("Inicia el proceso de obtencion de todos los asistentes.");
        return preferenceRepository.findAll();
    }

    /*
	 * Obtiene los datos de una instancia de Preference a partir de su ID.
	 *
	 * @param preferenceId Identificador de la instancia a consultar
	 * @return Instancia de PreferenceEntity con los datos del Preference consultado.
	 */

    @Transactional
    public PreferenceEntity getPreferenceById(Long preferenceId) throws EntityNotFoundException{
        log.info("Inicia el proceso de obtencion de un asistente = {0}", preferenceId);
        Optional<PreferenceEntity> preferenceEntity = preferenceRepository.findById(preferenceId);
        if(preferenceEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        log.info("Termina proceso de obtencion de un asistente = {0}", preferenceId);
        return preferenceEntity.get();
    }

    /*
	 * Actualiza la información de una instancia de Preference.
	 *
	 * @param preferenceId      Identificador de la instancia a actualizar
	 * @param preferenceEntity   Instancia de PreferenceEntity con los nuevos datos.
	 * @return                 Instancia de PreferenceEntity con los datos actualizados.
	 */

    @Transactional
    public PreferenceEntity updatePreference(Long preferenceId, PreferenceEntity preferenceEntity) throws EntityNotFoundException {
        log.info("Inicia el proceso de actualización de una preferencia = {}", preferenceId);
        Optional<PreferenceEntity> preference = preferenceRepository.findById(preferenceId);
        if(preference.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.PREFERENCE_NOT_FOUND);
        }
        preferenceEntity.setId(preferenceId);
    
        return preferenceRepository.save(preferenceEntity);
    }

    /*
	 * Elimina una instancia de Preference de la base de datos.
	 *
	 * @param preferenceId Identificador de la instancia a eliminar.
	 */

    @Transactional
    public void deletePreference(Long preferenceId) throws EntityNotFoundException, IllegalStateException {
        log.info("Inicia el proceso de eliminación de una preferencia = {0}", preferenceId);
        Optional<PreferenceEntity> preference = preferenceRepository.findById(preferenceId);
        if(preference.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        preferenceRepository.delete(preference.get());
        preferenceRepository.deleteById(preferenceId);;
    }
}
