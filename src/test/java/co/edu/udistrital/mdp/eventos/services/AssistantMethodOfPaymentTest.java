package co.edu.udistrital.mdp.eventos.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.eventos.entities.paymententity.CreditCardEntity;
import co.edu.udistrital.mdp.eventos.entities.paymententity.MethodOfPaymentEntity;
import co.edu.udistrital.mdp.eventos.entities.userentity.AssistantEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.paymententity.AssistantMethodOfPaymentService;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AssistantMethodOfPaymentService.class)
public class AssistantMethodOfPaymentTest {
    @Autowired
    private AssistantMethodOfPaymentService assistantMethodOfPaymentService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private AssistantEntity assistant1;
    private MethodOfPaymentEntity methodOfPayment1;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    void clearData() {
        entityManager.getEntityManager().createQuery("DELETE FROM MethodOfPaymentEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM AssistantEntity").executeUpdate();
    }

    void insertData() {
        assistant1 = factory.manufacturePojo(AssistantEntity.class);
        assistant1.setName("Assistant 1");
        entityManager.persist(assistant1);

        methodOfPayment1 = factory.manufacturePojo(CreditCardEntity.class);
        methodOfPayment1.setAssistant(assistant1);
        entityManager.persist(methodOfPayment1);
    }

    @Test
    void testAddMethodOfPayment() throws EntityNotFoundException {
        MethodOfPaymentEntity result = assistantMethodOfPaymentService.addMethodOfPayment(
                assistant1.getId(), methodOfPayment1.getId());

        assertNotNull(result);
        assertEquals(assistant1.getId(), result.getAssistant().getId());
    }

    @Test
    void testGetMethodOfPaymentById() throws EntityNotFoundException {
        methodOfPayment1.setAssistant(assistant1);
        entityManager.merge(methodOfPayment1);

        MethodOfPaymentEntity result = assistantMethodOfPaymentService.getMethodOfPaymentById(
                assistant1.getId(), methodOfPayment1.getId());

        assertNotNull(result);
        assertEquals(methodOfPayment1.getId(), result.getId());
    }

    @Test
    void testGetAllMethodsOfPayment() throws EntityNotFoundException {
        methodOfPayment1.setAssistant(assistant1);
        entityManager.merge(methodOfPayment1);

        List<MethodOfPaymentEntity> result = assistantMethodOfPaymentService.getAllMethodsOfPayment(
                assistant1.getId());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testRemoveMethodOfPayment() throws EntityNotFoundException {
        methodOfPayment1.setAssistant(assistant1);
        entityManager.merge(methodOfPayment1);

        assistantMethodOfPaymentService.removeMethodOfPayment(assistant1.getId(), methodOfPayment1.getId());

        MethodOfPaymentEntity updated = entityManager.find(MethodOfPaymentEntity.class, methodOfPayment1.getId());
        assertNull(updated.getAssistant());
    }

    @Test
    void testAddMethodOfPaymentInvalidAssistant() {
        assertThrows(EntityNotFoundException.class, () -> {
            assistantMethodOfPaymentService.addMethodOfPayment(0L, methodOfPayment1.getId());
        });
    }

    @Test
    void testAddMethodOfPaymentInvalidPayment() {
        assertThrows(EntityNotFoundException.class, () -> {
            assistantMethodOfPaymentService.addMethodOfPayment(assistant1.getId(), 0L);
        });
    }
}
