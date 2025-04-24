package co.edu.udistrital.mdp.eventos.services.userentity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.repositories.AssistantRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssistantService {

    @Autowired
    private AssistantRepository assistantRepository;

    /*
     * Se encarga de crear un nuevo asistente en la base de datos.
     * 
     * @param assistant Objeto de AuthorEntity con los datos nuevos.
     * @Return Objeto de AssistantEntity con los datos nuevos y su ID.
     * @throws Exception Si ocurre un error al crear el asistente.
     */

    @Transactional
    public AssistantEntity createAssistant(AssistantEntity assistant) {
        log.info("Inicia el proceso de creación de un nuevo asistente.");
        
        return assistantRepository.save(assistant);
    }


}
