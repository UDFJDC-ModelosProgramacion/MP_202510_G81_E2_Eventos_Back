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

import co.edu.udistrital.mdp.eventos.entities.userentity.OrganizerEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.userentity.OrganizerService;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(OrganizerService.class)
public class OrganizerServiceTest {

    @Autowired
    private OrganizerService organizerService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<OrganizerEntity> organizerList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from OrganizerEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            OrganizerEntity organizerEntity = factory.manufacturePojo(OrganizerEntity.class);
            entityManager.persist(organizerEntity);
            organizerList.add(organizerEntity);
        }
    }

    @Test
    void testCreateOrganizer() throws IllegalOperationException {
        OrganizerEntity newOrganizer = factory.manufacturePojo(OrganizerEntity.class);

        OrganizerEntity result = organizerService.createOrganizer(newOrganizer);
        assertNotNull(result);
        assertNotNull(result.getId());

        OrganizerEntity persisted = entityManager.find(OrganizerEntity.class, result.getId());
        assertEquals(newOrganizer.getEmail(), persisted.getEmail());
    }

    @Test
    void testGetAllOrganizers() {
        List<OrganizerEntity> list = organizerService.getAllOrganizers();
        assertEquals(organizerList.size(), list.size());
    }

    @Test
    void testGetOrganizerById() throws EntityNotFoundException {
        OrganizerEntity sample = organizerList.get(0);
        OrganizerEntity found = organizerService.getOrganizerById(sample.getId());
        assertNotNull(found);
        assertEquals(sample.getEmail(), found.getEmail());
    }

    @Test
    void testUpdateOrganizer() throws EntityNotFoundException {
        OrganizerEntity original = organizerList.get(0);
        OrganizerEntity updated = factory.manufacturePojo(OrganizerEntity.class);
        OrganizerEntity result = organizerService.updateOrganizer(original.getId(), updated);

        assertNotNull(result);
        assertEquals(original.getId(), result.getId());
        assertEquals(updated.getName(), result.getName());
    }

    @Test
    void testDeleteOrganizer() throws EntityNotFoundException {
        OrganizerEntity toDelete = organizerList.get(0);
        assertDoesNotThrow(() -> organizerService.deleteOrganizer(toDelete.getId()));
        OrganizerEntity deleted = entityManager.find(OrganizerEntity.class, toDelete.getId());
        assertNull(deleted);
    }

    @Test
    void testGetOrganizerByInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> organizerService.getOrganizerById(0L));
    }

    @Test
    void testUpdateOrganizerWithInvalidId() {
        OrganizerEntity updated = factory.manufacturePojo(OrganizerEntity.class);
        assertThrows(EntityNotFoundException.class, () -> organizerService.updateOrganizer(0L, updated));
    }

    @Test
    void testDeleteOrganizerWithInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> organizerService.deleteOrganizer(0L));
    }
}
