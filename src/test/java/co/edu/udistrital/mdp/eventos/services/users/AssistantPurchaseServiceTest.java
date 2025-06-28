package co.edu.udistrital.mdp.eventos.services.users;

import co.edu.udistrital.mdp.eventos.entities.bookingentity.PurchaseEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.userentity.booking.AssistantPurchaseService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@Transactional
@Import(AssistantPurchaseService.class)
public class AssistantPurchaseServiceTest {

    @Autowired
    private AssistantPurchaseService assistantPurchaseService;

    @Autowired
    private TestEntityManager entityManager;

    // Listas para almacenar la data insertada en las pruebas.
    private List<AssistantEntity> assistantData = new ArrayList<>();
    private List<PurchaseEntity> purchaseData = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia la data de las tablas involucradas en las pruebas.
     */
    private void clearData() {
        // Primero se borran las compras y luego los asistentes.
        entityManager.getEntityManager()
                    .createQuery("DELETE FROM PurchaseEntity")
                    .executeUpdate();
        entityManager.getEntityManager()
                    .createQuery("DELETE FROM AssistantEntity")
                    .executeUpdate();
    }

    /**
     * Inserta datos de prueba:
     * - Se crean dos asistentes.
     * - Se crean tres purchases:
     *   o purchase1 asociada a assistant1.
     *   o purchase2 sin asociación (para ser asociada en pruebas).
     *   o purchase3 asociada a assistant2.
     */
    private void insertData() {
        // Crear dos asistentes.
        AssistantEntity assistant1 = new AssistantEntity();
        assistant1.setName("Assistant 1");
        entityManager.persist(assistant1);
        assistantData.add(assistant1);

        AssistantEntity assistant2 = new AssistantEntity();
        assistant2.setName("Assistant 2");
        entityManager.persist(assistant2);
        assistantData.add(assistant2);

        // Crear tres purchases.
        // Purchase asociada a assistant1.
        PurchaseEntity purchase1 = new PurchaseEntity();
        purchase1.setRemainingSeats(10);
        purchase1.setAmount(100.0);
        purchase1.setPurchaseDate(LocalDateTime.now());
        purchase1.setAssistant(assistant1);
        entityManager.persist(purchase1);
        purchaseData.add(purchase1);
        assistant1.getPurchases().add(purchase1);

        // Purchase sin asociación.
        PurchaseEntity purchase2 = new PurchaseEntity();
        purchase2.setRemainingSeats(5);
        purchase2.setAmount(50.0);
        purchase2.setPurchaseDate(LocalDateTime.now());
        // No se asocia a ningún assistant.
        entityManager.persist(purchase2);
        purchaseData.add(purchase2);

        // Purchase asociada a assistant2 (para verificar error de asociación).
        PurchaseEntity purchase3 = new PurchaseEntity();
        purchase3.setRemainingSeats(8);
        purchase3.setAmount(80.0);
        purchase3.setPurchaseDate(LocalDateTime.now());
        purchase3.setAssistant(assistant2);
        entityManager.persist(purchase3);
        purchaseData.add(purchase3);
        assistant2.getPurchases().add(purchase3);

        entityManager.flush();
    }

    // -------------------- TESTS PARA addPurchase --------------------

    @Test
    public void testAddPurchaseValid() throws Exception {
        // Se asocia purchase2 (sin asociación) a assistant1.
        Long assistantId = assistantData.get(0).getId();
        Long purchaseId = purchaseData.get(1).getId();

        PurchaseEntity result = assistantPurchaseService.addPurchase(assistantId, purchaseId);
        assertNotNull(result);
        assertNotNull(result.getAssistant());
        assertEquals(assistantId, result.getAssistant().getId(), "La purchase debe quedar asociada a assistant1");
    }

    @Test
    public void testAddPurchaseInvalidAssistant() {
        Long invalidAssistantId = 999L;
        Long purchaseId = purchaseData.get(1).getId();

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantPurchaseService.addPurchase(invalidAssistantId, purchaseId);
        });
        assertEquals(ErrorMessage.ASSISTANT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testAddPurchaseInvalidPurchase() {
        Long assistantId = assistantData.get(0).getId();
        Long invalidPurchaseId = 999L;

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantPurchaseService.addPurchase(assistantId, invalidPurchaseId);
        });
        assertEquals("The purchase with the given id was not found", exception.getMessage());
    }

    // -------------------- TESTS PARA getPurchases --------------------

    @Test
    public void testGetPurchasesValid() throws Exception {
        // assistant1 tiene al menos una purchase asociada (purchase1)
        Long assistantId = assistantData.get(0).getId();
        List<PurchaseEntity> purchases = assistantPurchaseService.getPurchases(assistantId);
        assertNotNull(purchases);
        assertFalse(purchases.isEmpty(), "Debe devolver al menos una purchase");
        // Verificar que cada purchase esté asociada al mismo assistant.
        for (PurchaseEntity purchase : purchases) {
            assertEquals(assistantId, purchase.getAssistant().getId());
        }
    }

    @Test
    public void testGetPurchasesInvalidAssistant() {
        Long invalidAssistantId = 999L;
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantPurchaseService.getPurchases(invalidAssistantId);
        });
        assertEquals(ErrorMessage.ASSISTANT_NOT_FOUND, exception.getMessage());
    }

    // -------------------- TESTS PARA getPurchase --------------------

    @Test
    public void testGetPurchaseValid() throws Exception {
        // Se obtiene purchase1, el cual está asociado a assistant1.
        Long assistantId = assistantData.get(0).getId();
        Long purchaseId = purchaseData.get(0).getId();

        PurchaseEntity result = assistantPurchaseService.getPurchase(assistantId, purchaseId);
        assertNotNull(result);
        assertEquals(assistantId, result.getAssistant().getId());
    }

    @Test
    public void testGetPurchaseAssistantNotFound() {
        Long invalidAssistantId = 999L;
        Long purchaseId = purchaseData.get(0).getId();
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantPurchaseService.getPurchase(invalidAssistantId, purchaseId);
        });
        assertEquals(ErrorMessage.ASSISTANT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testGetPurchasePurchaseNotFound() {
        Long assistantId = assistantData.get(0).getId();
        Long invalidPurchaseId = 999L;
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantPurchaseService.getPurchase(assistantId, invalidPurchaseId);
        });
        assertEquals("The purchase with the given id was not found", exception.getMessage());
    }

    @Test
    public void testGetPurchaseNotAssociated() {
        // purchase3 está asociado a assistant2. Se intenta obtenerlo usando assistant1.
        Long assistant1Id = assistantData.get(0).getId();
        Long purchase3Id = purchaseData.get(2).getId();

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            assistantPurchaseService.getPurchase(assistant1Id, purchase3Id);
        });
        assertEquals("The purchase is not associated to the assistant", exception.getMessage());
    }

    // -------------------- TESTS PARA removePurchase --------------------

    @Test
    public void testRemovePurchaseValid() throws Exception {
        // Remover purchase1 de assistant1.
        AssistantEntity assistant = assistantData.get(0);
        PurchaseEntity purchase = purchaseData.get(0);
        Long assistantId = assistant.getId();
        Long purchaseId = purchase.getId();

        // Verificar asociación inicial.
        assertEquals(assistantId, purchase.getAssistant().getId());
        assertTrue(assistant.getPurchases().contains(purchase));

        // Remover la asociación.
        assistantPurchaseService.removePurchase(assistantId, purchaseId);

        // Refrescar las entidades.
        PurchaseEntity removed = entityManager.find(PurchaseEntity.class, purchaseId);
        AssistantEntity updatedAssistant = entityManager.find(AssistantEntity.class, assistantId);

        assertNull(removed.getAssistant(), "La purchase ya no debe estar asociada a ningún assistant");
        assertFalse(updatedAssistant.getPurchases().contains(removed), "El assistant ya no debe listar la purchase removida");
    }

    @Test
    public void testRemovePurchaseAssistantNotFound() {
        Long invalidAssistantId = 999L;
        Long purchaseId = purchaseData.get(0).getId();
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantPurchaseService.removePurchase(invalidAssistantId, purchaseId);
        });
        assertEquals(ErrorMessage.ASSISTANT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testRemovePurchasePurchaseNotFound() {
        Long assistantId = assistantData.get(0).getId();
        Long invalidPurchaseId = 999L;
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantPurchaseService.removePurchase(assistantId, invalidPurchaseId);
        });
        assertEquals("The purchase with the given id was not found", exception.getMessage());
    }

    @Test
    public void testRemovePurchaseNotAssociated() {
        // Se intenta remover una purchase que no está asociada a assistant1.
        // purchase3 está asociado a assistant2.
        Long assistant1Id = assistantData.get(0).getId();
        Long purchase3Id = purchaseData.get(2).getId();
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            assistantPurchaseService.removePurchase(assistant1Id, purchase3Id);
        });
        assertEquals("The purchase is not associated to the assistant", exception.getMessage());
    }
}
