package co.edu.udistrital.mdp.eventos.services.users;

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
    /*
    * Verifica que una preferencia válida pueda asociarse correctamente a un asistente.
    * Necesita: IDs válidos de asistente y preferencia.
    * Devuelve: la entidad de preferencia asociada.
    */
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

    /*
    * Verifica que se lance una excepción si se intenta asociar una preferencia con un ID de asistente inexistente.
    * Necesita: ID de asistente inválido, ID de preferencia válido.
    * Devuelve: EntityNotFoundException.
    */
    @Test
    void whenAddPreferenceWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        PreferenceEntity newPreference = factory.manufacturePojo(PreferenceEntity.class);
        entityManager.persist(newPreference);
        entityManager.flush();

        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.addPreference(999L, newPreference.getId());
        });
    }

    /*
     * Verifica que se lance una excepción si se intenta asociar una preferencia inexistente a un asistente válido.
     * Necesita: ID de asistente válido, ID de preferencia inválido.
     * Devuelve: EntityNotFoundException.
     */
    @Test
    void whenAddPreferenceWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.addPreference(assistant.getId(), 999L);
        });
    }

    // Tests para getPreferences

    /*
    * Verifica que se devuelvan todas las preferencias asociadas a un asistente válido.
    * Necesita: ID de asistente válido.
    * Devuelve: lista de preferencias asociadas.
    */
    @Test
    void whenGetPreferencesWithValidAssistantId_thenReturnPreferencesList() throws EntityNotFoundException{
        List<PreferenceEntity> result = assistantPreferenceService.getPreferences(assistant.getId());
        assertThat(result).hasSize(3).containsAll(preferenceList);
    }

    /*
    * Verifica que se lance una excepción al consultar preferencias de un asistente inexistente.
    * Necesita: ID de asistente inválido.
    * Devuelve: EntityNotFoundException.
    */
    @Test
    void whenGetPreferencesWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.getPreferences(999L);
        });
    }

    // Tests para getPreference 

    /*
    * Verifica que una preferencia asociada pueda recuperarse correctamente.
    * Necesita: IDs válidos de asistente y preferencia asociada.
    * Devuelve: entidad de preferencia.
    */
    @Test
    void whenGetPreferenceWithValidIdsAndAssociation_thenReturnPreference() throws EntityNotFoundException, IllegalOperationException {
        PreferenceEntity expectedPreference = preferenceList.get(0);
        PreferenceEntity result = assistantPreferenceService.getPreference(assistant.getId(), expectedPreference.getId());
        assertThat(result).isEqualTo(expectedPreference);
    }

    /*
    * Verifica que se lance una excepción si se intenta obtener una preferencia con un ID de asistente inválido.
    * Necesita: ID de asistente inválido.
    * Devuelve: EntityNotFoundException.
    */
    @Test
    void whenGetPreferenceWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.getPreference(999L, preferenceList.get(0).getId());
        });
    }

    /*
    * Verifica que se lance una excepción si se intenta obtener una preferencia con un ID de preferencia inválido.
    * Necesita: ID de preferencia inválido.
    * Devuelve: EntityNotFoundException.
    */
    @Test
    void whenGetPreferenceWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.getPreference(assistant.getId(), 999L);
        });
    }

    /*
    * Verifica que se lance una excepción si la preferencia no está asociada al asistente.
    * Necesita: IDs válidos, pero sin asociación.
    * Devuelve: IllegalOperationException.
    */
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

    /*
    * Verifica que una preferencia pueda ser correctamente desasociada de un asistente.
    * Necesita: IDs válidos con asociación previa.
    * Devuelve: la asociación se elimina exitosamente.
    */
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

    /*
    * Verifica que se lance una excepción si se intenta eliminar una preferencia con un ID de asistente inválido.
    * Necesita: ID de asistente inválido.
    * Devuelve: EntityNotFoundException.
    */
    @Test
    void whenRemovePreferenceWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.removePreference(999L, preferenceList.get(0).getId());
        });
    }

    /*
     * Verifica que se lance una excepción si se intenta eliminar una preferencia con un ID de preferencia inválido.
     * Necesita: ID de preferencia inválido.
     * Devuelve: EntityNotFoundException.
     */
    @Test
    void whenRemovePreferenceWithInvalidPreferenceId_thenThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.removePreference(assistant.getId(), 999L);
        });
    }

    /*
     * Verifica que se puedan reemplazar las preferencias asociadas a un asistente con una nueva lista válida.
     * Necesita: ID de asistente válido y lista de preferencias válidas.
     * Devuelve: nueva lista de preferencias asociadas.
     */
    @Test
    void whenAddPreferencesWithValidData_thenPreferencesAreReplaced() throws EntityNotFoundException {
        List<PreferenceEntity> newPreferences = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            PreferenceEntity preference = factory.manufacturePojo(PreferenceEntity.class);
            entityManager.persist(preference);
            newPreferences.add(preference);
        }

        List<PreferenceEntity> result = assistantPreferenceService.replacePreferences(assistant.getId(), newPreferences);

        assertEquals(newPreferences.size(), result.size());
        assertTrue(result.containsAll(newPreferences));
    }

    /*
     * Verifica que se lance una excepción si se intenta reemplazar las preferencias de un asistente inexistente.
     * Necesita: ID de asistente inválido.
     * Devuelve: EntityNotFoundException.
     */
    @Test
    void whenAddPreferencesWithInvalidAssistantId_thenThrowEntityNotFoundException() {
        PreferenceEntity preference = factory.manufacturePojo(PreferenceEntity.class);
        entityManager.persist(preference);

        List<PreferenceEntity> preferences = List.of(preference);

        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.replacePreferences(999L, preferences);
        });
    }

    /*
     * Verifica que se lance una excepción si alguna de las preferencias a asociar no existe.
     * Necesita: ID de asistente válido y lista con al menos una preferencia inválida.
     * Devuelve: EntityNotFoundException.
     */
    @Test
    void whenAddPreferencesWithNonExistentPreference_thenThrowEntityNotFoundException() {
        PreferenceEntity invalidPreference = factory.manufacturePojo(PreferenceEntity.class);
        invalidPreference.setId(999L); // No se persiste

        List<PreferenceEntity> preferences = List.of(invalidPreference);

        assertThrows(EntityNotFoundException.class, () -> {
            assistantPreferenceService.replacePreferences(assistant.getId(), preferences);
        });
    }
}