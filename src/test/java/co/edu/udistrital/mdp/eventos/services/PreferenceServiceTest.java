package co.edu.udistrital.mdp.eventos.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.PreferenceEntity;
import co.edu.udistrital.mdp.eventos.repositories.AssistantRepository;
import co.edu.udistrital.mdp.eventos.services.userentity.PreferenceService;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(PreferenceService.class)
public class PreferenceServiceTest {

    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AssistantRepository assistantRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<AssistantEntity> assistantList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PreferenceEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AssistantEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            AssistantEntity assistant = factory.manufacturePojo(AssistantEntity.class);
            entityManager.persist(assistant);
            assistantList.add(assistant);
        }
    }

    @Test
    void testCreatePreferenceWithAssistants() throws Exception {
        PreferenceEntity preference = factory.manufacturePojo(PreferenceEntity.class);
        preference.setAssistants(List.of(assistantList.get(0), assistantList.get(1)));

        PreferenceEntity result = preferenceService.createPreference(preference);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(preference.getDescription(), result.getDescription());
        assertEquals(2, result.getAssistants().size());
    }

    @Test
    void testCreatePreferenceWithoutDescriptionThrows() {
        PreferenceEntity preference = new PreferenceEntity();
        preference.setAssistants(List.of(assistantList.get(0)));

        assertThrows(IllegalArgumentException.class, () -> {
            preferenceService.createPreference(preference);
        });
    }

    @Test
    void testGetAllPreferences() throws Exception {
        PreferenceEntity preference = factory.manufacturePojo(PreferenceEntity.class);
        preference.setAssistants(List.of(assistantList.get(0)));
        entityManager.persist(preference);

        List<PreferenceEntity> preferences = preferenceService.getAllPreference();
        assertFalse(preferences.isEmpty());
    }

    @Test
    void testGetPreferenceById() throws Exception {
        PreferenceEntity preference = factory.manufacturePojo(PreferenceEntity.class);
        preference.setAssistants(List.of(assistantList.get(0)));
        PreferenceEntity persisted = entityManager.persist(preference);

        PreferenceEntity found = preferenceService.getPreferenceById(persisted.getId());
        assertNotNull(found);
        assertEquals(persisted.getId(), found.getId());
    }

    //@Test
    //void testUpdatePreference() throws Exception {
    //    PreferenceEntity preference = factory.manufacturePojo(PreferenceEntity.class);
    //    preference.setAssistants(List.of(assistantList.get(0)));
    //    PreferenceEntity persisted = entityManager.persist(preference);
//
    //    PreferenceEntity updated = factory.manufacturePojo(PreferenceEntity.class);
    //    updated.setAssistants(List.of(assistantList.get(1), assistantList.get(2)));
//
    //    PreferenceEntity result = preferenceService.updatePreference(persisted.getId(), updated);
    //    assertEquals(updated.getDescription(), result.getDescription());
    //    assertEquals(2, result.getAssistants().size());
    //}

    @Test
    void testUpdatePreference() throws Exception {
        // Crear y persistir una preferencia con asistentes
        PreferenceEntity preference = factory.manufacturePojo(PreferenceEntity.class);
        preference.setAssistants(List.of(assistantList.get(0), assistantList.get(1)));
        PreferenceEntity persisted = entityManager.persist(preference);
    
        // Crear un nuevo objeto con la descripción actualizada
        PreferenceEntity updated = new PreferenceEntity();
        updated.setDescription("Descripción actualizada");
    
        // Ejecutar el servicio
        PreferenceEntity result = preferenceService.updatePreference(persisted.getId(), updated);
    
        // Verificar que la descripción se haya actualizado
        assertEquals("Descripción actualizada", result.getDescription());
    
        // Verificar que los asistentes no hayan cambiado
        assertEquals(2, result.getAssistants().size());
        assertTrue(result.getAssistants().containsAll(persisted.getAssistants()));
    }


    @Test
    void testDeletePreference() throws Exception {
        PreferenceEntity preference = factory.manufacturePojo(PreferenceEntity.class);
        preference.setAssistants(List.of(assistantList.get(0)));
        PreferenceEntity persisted = entityManager.persist(preference);

        preferenceService.deletePreference(persisted.getId());

        assertFalse(preferenceService.getAllPreference().stream()
            .anyMatch(p -> p.getId().equals(persisted.getId())));
    }
}
