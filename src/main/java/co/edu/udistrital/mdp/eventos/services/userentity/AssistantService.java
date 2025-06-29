package co.edu.udistrital.mdp.eventos.services.userentity;

import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.dto.userdto.AssistantDetailDTO;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.repositories.AssistantRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssistantService {

    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private ModelMapper modelMapper;

    /*
     * Se encarga de crear un nuevo asistente en la base de datos.
     * 
     * @param assistant Objeto de AssistantEntity con los datos nuevos.
     * @Return Objeto de AssistantEntity con los datos nuevos y su ID.
     * @throws Exception Si ocurre un error al crear el asistente.
     */

    @Transactional
    public AssistantEntity createAssistant(AssistantEntity assistant) throws IllegalOperationException{
        log.info("Inicia el proceso de creación de un nuevo Assistant.");

        //*Validación: verificar si ya existe un asistente con el mismo correo
        if (assistantRepository.findByEmail(assistant.getEmail()).isPresent()) {
            throw new IllegalOperationException("Ya existe un asistente con este correo.");
        }
    
        // *Validación básica de campos obligatorios
        if (assistant.getName() == null || assistant.getEmail() == null || assistant.getPassword() == null) {
            throw new IllegalOperationException("Faltan datos obligatorios para crear el asistente.");
        }
    
        log.info("Finaliza el proceso de creación del asistente con ID: {}", assistant.getId());
        return assistantRepository.save(assistant);
    }


    /*
	 * Obtiene la lista de los registros de Assistant.
	 *
	 * @return Colección de objetos de AssistantEntity.
	 */

    @Transactional
    public List<AssistantEntity> getAllAssistants() {
        log.info("Inicia el proceso de obtencion de todos los asistentes.");
        return assistantRepository.findAll();
    }


    /*
	 * Obtiene los datos de una instancia de Assistant a partir de su ID.
	 *
	 * @param assistantId Identificador de la instancia a consultar
	 * @return Instancia de AssistantEntity con los datos del Assistant consultado.
	 */

    @Transactional
    public AssistantEntity getAssistantById(Long assistantId) throws EntityNotFoundException{
        log.info("Inicia el proceso de obtencion de un asistente = {0}", assistantId);
        Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
        if(assistantEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        log.info("Termina proceso de obtencion de un asistente = {0}", assistantId);
        return assistantEntity.get();
    }

    /*
	 * Obtiene los datos de una instancia de Assistant a partir de su EMAIL.
	 *
	 * @param assistantEmail Identificador de la instancia a consultar
	 * @return Instancia de AssistantEntity con los datos del Assistant consultado.
	 */

    @Transactional
    public AssistantEntity getAssistantByEmail(String assistantEmail) throws EntityNotFoundException {
        log.info("Inicia proceso de búsqueda de asistente por email: {}", assistantEmail);
        Optional<AssistantEntity> assistantEntity = assistantRepository.findByEmail(assistantEmail);
        if (assistantEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        return assistantEntity.get();
    }


    /*
	 * Actualiza la información de una instancia de Assistant.
	 *
	 * @param assistantId      Identificador de la instancia a actualizar
	 * @param assitantEntity   Instancia de AssistantEntity con los nuevos datos.
	 * @return                 Instancia de AssistantEntity con los datos actualizados.
	 */

    @Transactional
    public AssistantEntity updateAssistant(Long assistantId, AssistantEntity assistant) throws EntityNotFoundException {
        log.info("Inicia el proceso de actualización de un asistente = {0}", assistantId);
        Optional<AssistantEntity> assistantEntity = assistantRepository.findById(assistantId);
        if(assistantEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        assistant.setId(assistantId);
        return assistantRepository.save(assistant);
    }

    /*
	 * Elimina una instancia de Assistant de la base de datos.
	 *
	 * @param assitantId Identificador de la instancia a eliminar.
	 */

    @Transactional
    public void deleteAssistant(Long assistantId) throws EntityNotFoundException, IllegalStateException {
        log.info("Inicia el proceso de eliminación de un asistente = {0}", assistantId);
        Optional<AssistantEntity> assistant = assistantRepository.findById(assistantId);
        if(assistant.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        assistantRepository.delete(assistant.get());
        assistantRepository.deleteById(assistantId);;
    }

        @Transactional(readOnly = true)
    public List<AssistantDetailDTO> getAllAssistantsWithPayments() {
        List<AssistantEntity> assistants = assistantRepository.findAll();
        
        // Forzar carga de relaciones LAZY
        assistants.forEach(assistant -> {
            Hibernate.initialize(assistant.getPaymentMethods());
            // Inicializar otras relaciones si es necesario
        });
        
        return modelMapper.map(assistants, new TypeToken<List<AssistantDetailDTO>>(){}.getType());
    }

    @Transactional(readOnly = true)
    public AssistantDetailDTO getAssistantWithPayments(Long id) throws EntityNotFoundException {
        AssistantEntity assistant = assistantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assistant not found"));
        
        // Forzar carga de relaciones
        Hibernate.initialize(assistant.getPaymentMethods());
        // Inicializar otras relaciones si es necesario
        
        return modelMapper.map(assistant, AssistantDetailDTO.class);
    }
}
