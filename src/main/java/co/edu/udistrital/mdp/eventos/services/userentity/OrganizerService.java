package co.edu.udistrital.mdp.eventos.services.userentity;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.OrganizerEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.repositories.OrganizerRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrganizerService {

    @Autowired
    private OrganizerRepository organizerRepository;

    /*
     * Se encarga de crear un nuevo organizador en la base de datos.
     * 
     * @param organizer Objeto de AuthorEntity con los datos nuevos.
     * @Return Objeto de OrganizerEntity con los datos nuevos y su ID.
     * @throws Exception Si ocurre un error al crear el organizer.
     */

    //@Transactional
    //public AssistantEntity createAssistant(AssistantEntity assistant) throws IllegalOperationException{
    //    log.info("Inicia el proceso de creación de un nuevo asistente.");
    //    
    //    return assistantRepository.save(assistant);
    //}

    /*
	 * Obtiene la lista de los registros de OrganizerEntity.
	 *
	 * @return Colección de objetos de OrganizerEntity.
	 */

    @Transactional
    public OrganizerEntity getAssistantById(Long assistantId) throws EntityNotFoundException{
        log.info("Inicia el proceso de obtencion de un asistente = {0}", assistantId);
        Optional<OrganizerEntity> organizerEntity = organizerRepository.findById(assistantId);
        if(organizerEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        log.info("Termina proceso de obtencion de un asistente = {0}", assistantId);
        return organizerEntity.get();
    }

    /*
	 * Actualiza la información de una instancia de Organizer.
	 *
	 * @param organizerId      Identificador de la instancia a actualizar
	 * @param organizerEntity  Instancia de OrganizerEntity con los nuevos datos.
	 * @return                 Instancia de OrganizerEntity con los datos actualizados.
	 */

    @Transactional
    public OrganizerEntity updateAssistant(Long organizerId, OrganizerEntity organizerEntity) throws EntityNotFoundException {
        log.info("Inicia el proceso de actualización de un asistente = {0}", organizerId);
        Optional<OrganizerEntity> organizer = organizerRepository.findById(organizerId);
        if(organizer.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        organizerEntity.setId(organizerId);
        return organizerRepository.save(organizerEntity);
    }

    /*
	 * Elimina una instancia de Organizer de la base de datos.
	 *
	 * @param organizertId Identificador de la instancia a eliminar.
	 */

    @Transactional
    public void deleteOrganizer(Long organizerId) throws EntityNotFoundException, IllegalStateException {
        log.info("Inicia el proceso de eliminación de un asistente = {0}", organizerId);
        Optional<OrganizerEntity> organizer = organizerRepository.findById(organizerId);
        if(organizer.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSISTANT_NOT_FOUND);
        }
        organizerRepository.delete(organizer.get());
        organizerRepository.deleteById(organizerId);;
    }
}

