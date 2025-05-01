package co.edu.udistrital.mdp.eventos.services;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.*;

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
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.userentity.AssistantService;
import co.edu.udistrital.mdp.eventos.services.userentity.PreferenceAssistantService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Import({ PreferenceAssistantService.class, AssistantService.class })
public class PreferenceAssistantServiceTest {

    @Autowired
    private PreferenceAssistantService preferenceAssistantService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private PreferenceEntity preference;
    private List<AssistantEntity> assistantList = new ArrayList<>();

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
        preference = factory.manufacturePojo(PreferenceEntity.class);
        entityManager.persist(preference);

        for (int i = 0; i < 3; i++) {
            AssistantEntity assistant = factory.manufacturePojo(AssistantEntity.class);
            assistant.getPreferences().add(preference);
            entityManager.persist(assistant);
            assistantList.add(assistant);
            preference.getAssistants().add(assistant);
        }
    }

    // Tests para addAssistant
    @Test
    void whenAddAssistantWithValidIds_thenAssistantIsAdded() throws EntityNotFoundException, IllegalOperationException{
        PreferenceEntity newPreference = factory.manufacturePojo(PreferenceEntity.class);
		entityManager.persist(newPreference);
		
		AssistantEntity assistant = factory.manufacturePojo(AssistantEntity.class);
		entityManager.persist(assistant);
		
		preferenceAssistantService.addAssistant(newPreference.getId(), assistant.getId());
		
		AssistantEntity lastAssistant = preferenceAssistantService.getAssistant(newPreference.getId(), assistant.getId());
		assertEquals(assistant.getId(), lastAssistant.getId());
		assertEquals(assistant.getBirthDate(), lastAssistant.getBirthDate());
		assertEquals(assistant.getName(), lastAssistant.getName());
        //AssistantEntity newAssistant = factory.manufacturePojo(AssistantEntity.class);
        //entityManager.persist(newAssistant);
        //entityManager.flush();
//
        //AssistantEntity result = preferenceAssistantService.addAssistant(preference.getId(), newAssistant.getId());
//
        //assertThat(result.getPreferences()).contains(preference);
        //
        //entityManager.flush();
        //entityManager.clear();
        //PreferenceEntity reloadedPreference = entityManager.find(PreferenceEntity.class, preference.getId());
        //assertThat(reloadedPreference.getAssistants()).contains(newAssistant);
    }

    @Test
    void whenAddAssistantWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        AssistantEntity newAssistant = factory.manufacturePojo(AssistantEntity.class);
        entityManager.persist(newAssistant);
        entityManager.flush();

        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.addAssistant(999L, newAssistant.getId());
        });
    }

    @Test
    void whenAddAssistantWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.addAssistant(preference.getId(), 999L);
        });
    }

    // Tests para getAssistants
    @Test
    void whenGetAssistantsWithValidPreferenceId_thenReturnAssistantsList() throws EntityNotFoundException{
        List<AssistantEntity> result = preferenceAssistantService.getAssistants(preference.getId());
        assertThat(result).hasSize(3).containsAll(assistantList);
    }

    @Test
    void whenGetAssistantsWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.getAssistants(999L);
        });
    }

    // Tests para getAssistant
    @Test
    void whenGetAssistantWithValidIdsAndAssociation_thenReturnAssistant() throws EntityNotFoundException, IllegalOperationException{
        AssistantEntity expectedAssistant = assistantList.get(0);
        AssistantEntity result = preferenceAssistantService.getAssistant(preference.getId(), expectedAssistant.getId());
        assertThat(result).isEqualTo(expectedAssistant);
    }

    @Test
    void whenGetAssistantWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.getAssistant(999L, assistantList.get(0).getId());
        });
    }

    @Test
    void whenGetAssistantWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.getAssistant(preference.getId(), 999L);
        });
    }

    @Test
    void whenGetAssistantNotAssociated_thenThrowIllegalOperationException() {
        AssistantEntity newAssistant = factory.manufacturePojo(AssistantEntity.class);
        entityManager.persist(newAssistant);
        entityManager.flush();

        assertThrows(IllegalOperationException.class, () -> {
            preferenceAssistantService.getAssistant(preference.getId(), newAssistant.getId());
        });
    }

    // Tests para removeAssistant
    @Test
    void whenRemoveAssistantWithValidIds_thenAssistantIsRemoved() throws EntityNotFoundException{
        AssistantEntity toRemove = assistantList.get(0);
        preferenceAssistantService.removeAssistant(preference.getId(), toRemove.getId());

        entityManager.flush();
        entityManager.clear();

        PreferenceEntity reloadedPreference = entityManager.find(PreferenceEntity.class, preference.getId());
        assertThat(reloadedPreference.getAssistants()).doesNotContain(toRemove);
    }

    @Test
    void whenRemoveAssistantWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.removeAssistant(999L, assistantList.get(0).getId());
        });
    }

    @Test
    void whenRemoveAssistantWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.removeAssistant(preference.getId(), 999L);
        });
    }
}
