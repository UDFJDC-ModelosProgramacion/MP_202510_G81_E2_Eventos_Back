package co.edu.udistrital.mdp.eventos.services.userentity;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.userentity.OrganizerEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
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
     * @param organizer Objeto de organizerEntity con los datos nuevos.
     * @Return Objeto de OrganizerEntity con los datos nuevos y su ID.
     * @throws Exception Si ocurre un error al crear el organizer.
     */

    @Transactional
    public OrganizerEntity createOrganizer(OrganizerEntity organizer) throws IllegalOperationException{
        log.info("Inicia el proceso de creación de un nuevo Organizer.");

        //*Validación: verificar si ya existe un Organizer con el mismo correo
        if (organizerRepository.findByEmail(organizer.getEmail()).isPresent()) {
            throw new IllegalOperationException("Ya existe un asistente con este correo.");
        }
    
        // *Validación básica de campos obligatorios
        if (organizer.getName() == null || organizer.getEmail() == null || organizer.getPassword() == null) {
            throw new IllegalOperationException("Faltan datos obligatorios para crear el asistente.");
        }
    
        log.info("Finaliza el proceso de creación del Organizer con ID: {}", organizer.getId());
        return organizerRepository.save(organizer);
    }


    /*
	 * Obtiene la lista de los registros de Assistant.
	 *
	 * @return Colección de objetos de AssistantEntity.
	 */

    @Transactional
    public List<OrganizerEntity> getAllOrganizers() {
        log.info("Inicia el proceso de obtencion de todos los Organizer.");
        return organizerRepository.findAll();
    }


    /*
	 * Obtiene la lista de los registros de OrganizerEntity.
	 *
	 * @return Colección de objetos de OrganizerEntity.
	 */

    @Transactional
    public OrganizerEntity getOrganizerById(Long organizerId) throws EntityNotFoundException{
        log.info("Inicia el proceso de obtencion de un Organizer = {0}", organizerId);
        Optional<OrganizerEntity> organizerEntity = organizerRepository.findById(organizerId);
        if(organizerEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ORGANIZER_NOT_FOUND);
        }
        log.info("Termina proceso de obtencion de un Organizer = {0}", organizerId);
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
    public OrganizerEntity updateOrganizer(Long organizerId, OrganizerEntity organizerEntity) throws EntityNotFoundException {
        log.info("Inicia el proceso de actualización de un Organizer = {0}", organizerId);
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

