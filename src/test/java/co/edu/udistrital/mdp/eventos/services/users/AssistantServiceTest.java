package co.edu.udistrital.mdp.eventos.services.users;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.userentity.AssistantService;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AssistantService.class)
public class AssistantServiceTest {

    @Autowired
    private AssistantService assistantService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<AssistantEntity> assistantList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AssistantEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            AssistantEntity assistantEntity = factory.manufacturePojo(AssistantEntity.class);
            entityManager.persist(assistantEntity);
            assistantList.add(assistantEntity);
        }
    }

    @Test
    void testCreateAssistant() throws IllegalOperationException {
        AssistantEntity newAssistant = factory.manufacturePojo(AssistantEntity.class);

        AssistantEntity result = assistantService.createAssistant(newAssistant);
        assertNotNull(result);
        assertNotNull(result.getId());

        AssistantEntity persisted = entityManager.find(AssistantEntity.class, result.getId());
        assertEquals(newAssistant.getEmail(), persisted.getEmail());
    }

    @Test
    void testGetAllAssistants() {
        List<AssistantEntity> list = assistantService.getAllAssistants();
        assertEquals(assistantList.size(), list.size());
    }

    @Test
    void testGetAssistantById() throws EntityNotFoundException {
        AssistantEntity sample = assistantList.get(0);
        AssistantEntity found = assistantService.getAssistantById(sample.getId());
        assertNotNull(found);
        assertEquals(sample.getEmail(), found.getEmail());
    }

    @Test
    void testUpdateAssistant() throws EntityNotFoundException {
        AssistantEntity original = assistantList.get(0);
        AssistantEntity updated = factory.manufacturePojo(AssistantEntity.class);
        AssistantEntity result = assistantService.updateAssistant(original.getId(), updated);

        assertNotNull(result);
        assertEquals(original.getId(), result.getId());
        assertEquals(updated.getName(), result.getName());
    }

    @Test
    void testDeleteAssistant() throws EntityNotFoundException {
        AssistantEntity toDelete = assistantList.get(0);
        assertDoesNotThrow(() -> assistantService.deleteAssistant(toDelete.getId()));
        AssistantEntity deleted = entityManager.find(AssistantEntity.class, toDelete.getId());
        assertNull(deleted);
    }

    @Test
    void testGetAssistantByInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> assistantService.getAssistantById(0L));
    }

    @Test
    void testUpdateAssistantWithInvalidId() {
        AssistantEntity updated = factory.manufacturePojo(AssistantEntity.class);
        assertThrows(EntityNotFoundException.class, () -> assistantService.updateAssistant(0L, updated));
    }

    @Test
    void testDeleteAssistantWithInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> assistantService.deleteAssistant(0L));
    }
}
