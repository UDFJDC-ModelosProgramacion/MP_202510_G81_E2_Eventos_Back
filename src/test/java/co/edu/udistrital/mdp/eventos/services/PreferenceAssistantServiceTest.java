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

    /**
     * Verifica que un asistente válido sea correctamente asociado a una preferencia existente.
     */
    @Test
    void whenAddAssistantWithValidIds_thenAssistantIsAdded() throws EntityNotFoundException, IllegalOperationException {
        PreferenceEntity newPreference = factory.manufacturePojo(PreferenceEntity.class);
        entityManager.persist(newPreference);

        AssistantEntity assistant = factory.manufacturePojo(AssistantEntity.class);
        entityManager.persist(assistant);

        preferenceAssistantService.addAssistant(newPreference.getId(), assistant.getId());

        AssistantEntity lastAssistant = preferenceAssistantService.getAssistant(newPreference.getId(), assistant.getId());
        assertEquals(assistant.getId(), lastAssistant.getId());
        assertEquals(assistant.getBirthDate(), lastAssistant.getBirthDate());
        assertEquals(assistant.getName(), lastAssistant.getName());
    }

    /**
     * Verifica que se lance una EntityNotFoundException cuando el ID de preferencia es inválido.
     */
    @Test
    void whenAddAssistantWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        AssistantEntity newAssistant = factory.manufacturePojo(AssistantEntity.class);
        entityManager.persist(newAssistant);
        entityManager.flush();

        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.addAssistant(999L, newAssistant.getId());
        });
    }

    /**
     * Verifica que se lance una EntityNotFoundException cuando el ID del asistente es inválido.
     */
    @Test
    void whenAddAssistantWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.addAssistant(preference.getId(), 999L);
        });
    }

    // Tests para getAssistants

    /**
     * Verifica que se devuelva correctamente la lista de asistentes asociados a una preferencia válida.
     */
    @Test
    void whenGetAssistantsWithValidPreferenceId_thenReturnAssistantsList() throws EntityNotFoundException {
        List<AssistantEntity> result = preferenceAssistantService.getAssistants(preference.getId());
        assertThat(result).hasSize(3).containsAll(assistantList);
    }

    /**
     * Verifica que se lance una EntityNotFoundException si el ID de preferencia no existe.
     */
    @Test
    void whenGetAssistantsWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.getAssistants(999L);
        });
    }

    // Tests para getAssistant

    /**
     * Verifica que se retorne un asistente correctamente cuando está asociado a la preferencia indicada.
     */
    @Test
    void whenGetAssistantWithValidIdsAndAssociation_thenReturnAssistant() throws EntityNotFoundException, IllegalOperationException {
        AssistantEntity expectedAssistant = assistantList.get(0);
        AssistantEntity result = preferenceAssistantService.getAssistant(preference.getId(), expectedAssistant.getId());
        assertThat(result).isEqualTo(expectedAssistant);
    }

    /**
     * Verifica que se lance EntityNotFoundException cuando el ID de preferencia no existe.
     */
    @Test
    void whenGetAssistantWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.getAssistant(999L, assistantList.get(0).getId());
        });
    }

    /**
     * Verifica que se lance EntityNotFoundException cuando el ID del asistente no existe.
     */
    @Test
    void whenGetAssistantWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.getAssistant(preference.getId(), 999L);
        });
    }

    /**
     * Verifica que se lance IllegalOperationException si el asistente no está asociado a la preferencia.
     */
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

    /**
     * Verifica que se pueda remover correctamente un asistente asociado a una preferencia.
     */
    @Test
    void whenRemoveAssistantWithValidIds_thenAssistantIsRemoved() throws EntityNotFoundException {
        AssistantEntity toRemove = assistantList.get(0);
        preferenceAssistantService.removeAssistant(preference.getId(), toRemove.getId());

        entityManager.flush();
        entityManager.clear();

        PreferenceEntity reloadedPreference = entityManager.find(PreferenceEntity.class, preference.getId());
        assertThat(reloadedPreference.getAssistants()).doesNotContain(toRemove);
    }

    /**
     * Verifica que se lance EntityNotFoundException si el ID de preferencia no existe.
     */
    @Test
    void whenRemoveAssistantWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.removeAssistant(999L, assistantList.get(0).getId());
        });
    }

    /**
     * Verifica que se lance EntityNotFoundException si el ID del asistente no existe.
     */
    @Test
    void whenRemoveAssistantWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.removeAssistant(preference.getId(), 999L);
        });
    }

    // Tests para replaceAssistants

    /**
     * Verifica que el método replaceAssistants reemplace correctamente todos los asistentes asociados
     * a una preferencia dada cuando se proporcionan IDs válidos.
     */

    @Test
    void whenReplaceAssistantsWithValidIds_thenAssistantsAreReplaced() throws EntityNotFoundException {
        List<AssistantEntity> newAssistants = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            AssistantEntity assistant = factory.manufacturePojo(AssistantEntity.class);
            entityManager.persist(assistant);
            newAssistants.add(assistant);
        }

        List<AssistantEntity> result = preferenceAssistantService.replaceAssistants(preference.getId(), newAssistants);

        // Comparación por ID para evitar StackOverflowError
        List<Long> resultIds = result.stream().map(AssistantEntity::getId).toList();
        List<Long> expectedIds = newAssistants.stream().map(AssistantEntity::getId).toList();
        assertThat(resultIds).containsExactlyInAnyOrderElementsOf(expectedIds);

        entityManager.flush();
        entityManager.clear();

        PreferenceEntity updatedPreference = entityManager.find(PreferenceEntity.class, preference.getId());
        List<Long> updatedIds = updatedPreference.getAssistants().stream().map(AssistantEntity::getId).toList();
        assertThat(updatedIds).containsExactlyInAnyOrderElementsOf(expectedIds);
    }

    /**
     * Verifica que se lance una EntityNotFoundException cuando se intenta reemplazar
     * asistentes en una preferencia que no existe.
     */
    @Test
    void whenReplaceAssistantsWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        AssistantEntity assistant = factory.manufacturePojo(AssistantEntity.class);
        entityManager.persist(assistant);
        List<AssistantEntity> newAssistants = List.of(assistant);

        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.replaceAssistants(999L, newAssistants);
        });
    }

    /**
     * Verifica que se lance una EntityNotFoundException cuando se intenta reemplazar
     * asistentes incluyendo uno con un ID inválido.
     */
    @Test
    void whenReplaceAssistantsWithNonExistingAssistant_thenThrowEntityNotFoundException() {
        AssistantEntity existingAssistant = factory.manufacturePojo(AssistantEntity.class);
        entityManager.persist(existingAssistant);

        AssistantEntity nonExistentAssistant = factory.manufacturePojo(AssistantEntity.class);
        nonExistentAssistant.setId(999L); // ID inexistente

        List<AssistantEntity> newAssistants = List.of(existingAssistant, nonExistentAssistant);

        assertThrows(EntityNotFoundException.class, () -> {
            preferenceAssistantService.replaceAssistants(preference.getId(), newAssistants);
        });
    }
}

