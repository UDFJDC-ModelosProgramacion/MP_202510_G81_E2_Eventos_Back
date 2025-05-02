package co.edu.udistrital.mdp.eventos.services;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.PreferenceEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.userentity.AssistantPreferenceService;
import co.edu.udistrital.mdp.eventos.services.userentity.PreferenceService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import({AssistantPreferenceService.class, PreferenceService.class})
public class AssistantPreferenceServiceTest {

    @Autowired
    private AssistantPreferenceService assistantPreferenceService;

    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private AssistantEntity assistant;
    private List<PreferenceEntity> preferenceList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("DELETE FROM PreferenceEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM AssistantEntity").executeUpdate();
    }

    private void insertData() {
        assistant = factory.manufacturePojo(AssistantEntity.class);
        entityManager.persist(assistant);

        for (int i = 0; i < 3; i++) {
            PreferenceEntity preference = factory.manufacturePojo(PreferenceEntity.class);
            preference.getAssistants().add(assistant);
            entityManager.persist(preference);
            preferenceList.add(preference);
            assistant.getPreferences().add(preference);
        }
    }

    // Tests para addPreference
    @Test
    void whenAddPreferenceWithValidIds_thenPreferenceIsAdded() throws EntityNotFoundException, IllegalOperationException{
        PreferenceEntity newPreference = factory.manufacturePojo(PreferenceEntity.class);
        preferenceService.createPreference(newPreference);

        PreferenceEntity preferenceEntity = assistantPreferenceService.addPreference(assistant.getId(), newPreference.getId());
        assertNotNull(preferenceEntity);

		assertEquals(preferenceEntity.getId(), newPreference.getId());
		assertEquals(preferenceEntity.getDescription(), newPreference.getDescription());

		PreferenceEntity lastPreference = assistantPreferenceService.getPreference(assistant.getId(), newPreference.getId());

		assertEquals(lastPreference.getId(), newPreference.getId());
		assertEquals(lastPreference.getDescription(), newPreference.getDescription());
    }

    @Test
    void whenAddPreferenceWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        PreferenceEntity newPreference = factory.manufacturePojo(PreferenceEntity.class);
        entityManager.persist(newPreference);
        entityManager.flush();

        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.addPreference(999L, newPreference.getId());
        });
    }

    @Test
    void whenAddPreferenceWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.addPreference(assistant.getId(), 999L);
        });
    }

    // Tests para getPreferences
    @Test
    void whenGetPreferencesWithValidAssistantId_thenReturnPreferencesList() throws EntityNotFoundException{
        List<PreferenceEntity> result = assistantPreferenceService.getPreferences(assistant.getId());
        assertThat(result).hasSize(3).containsAll(preferenceList);
    }

    @Test
    void whenGetPreferencesWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.getPreferences(999L);
        });
    }

    // Tests para getPreference (corregir nombre del método si es necesario)
    @Test
    void whenGetPreferenceWithValidIdsAndAssociation_thenReturnPreference() throws EntityNotFoundException, IllegalOperationException {
        PreferenceEntity expectedPreference = preferenceList.get(0);
        PreferenceEntity result = assistantPreferenceService.getPreference(assistant.getId(), expectedPreference.getId());
        assertThat(result).isEqualTo(expectedPreference);
    }

    @Test
    void whenGetPreferenceWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.getPreference(999L, preferenceList.get(0).getId());
        });
    }

    @Test
    void whenGetPreferenceWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.getPreference(assistant.getId(), 999L);
        });
    }

    @Test
    void whenGetPreferenceNotAssociated_thenThrowIllegalOperationException() {
        PreferenceEntity newPreference = factory.manufacturePojo(PreferenceEntity.class);
        entityManager.persist(newPreference);
        entityManager.flush();

        assertThrows(IllegalOperationException.class, () -> {
            assistantPreferenceService.getPreference(assistant.getId(), newPreference.getId());
        });
    }

    // Tests para removePreference
    @Test
    void whenRemovePreferenceWithValidIds_thenPreferenceIsRemoved() throws EntityNotFoundException {
        PreferenceEntity toRemove = preferenceList.get(0);
        assistantPreferenceService.removePreference(assistant.getId(), toRemove.getId());

        entityManager.flush();
        entityManager.clear();

        AssistantEntity reloadedAssistant = entityManager.find(AssistantEntity.class, assistant.getId());
        PreferenceEntity reloadedPreference = entityManager.find(PreferenceEntity.class, toRemove.getId());

        assertThat(reloadedAssistant.getPreferences()).doesNotContain(reloadedPreference);
        assertThat(reloadedPreference.getAssistants()).doesNotContain(reloadedAssistant);
    }

    @Test
    void whenRemovePreferenceWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.removePreference(999L, preferenceList.get(0).getId());
        });
    }

    @Test
    void whenRemovePreferenceWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.removePreference(assistant.getId(), 999L);
        });
    }
}