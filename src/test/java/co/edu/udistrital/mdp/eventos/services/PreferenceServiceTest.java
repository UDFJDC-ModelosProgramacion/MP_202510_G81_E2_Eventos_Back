package co.edu.udistrital.mdp.eventos.services;

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
import co.edu.udistrital.mdp.eventos.entities.userentity.PreferenceEntity;
import co.edu.udistrital.mdp.eventos.services.userentity.PreferenceService;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(PreferenceService.class)
public class PreferenceServiceTest {

    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<AssistantEntity> assistantList = new ArrayList<>();

    private List<PreferenceEntity> preferenceList = new ArrayList<>();

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
            PreferenceEntity preference = factory.manufacturePojo(PreferenceEntity.class);
            entityManager.persist(assistant);
            assistantList.add(assistant);

            entityManager.persist(preference);
            preferenceList.add(preference);
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

    @Test
    void testUpdatePreference() throws Exception {
        PreferenceEntity original = preferenceList.get(0);
        PreferenceEntity newPreference = factory.manufacturePojo(PreferenceEntity.class);
        newPreference.setId(original.getId());
        preferenceService.updatePreference(original.getId(), newPreference);

        PreferenceEntity resp = entityManager.find(PreferenceEntity.class, original.getId());
        assertEquals(newPreference.getId(),resp.getId());
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
