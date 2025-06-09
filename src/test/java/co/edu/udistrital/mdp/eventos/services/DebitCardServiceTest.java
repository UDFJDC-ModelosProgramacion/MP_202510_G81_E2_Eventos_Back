package co.edu.udistrital.mdp.eventos.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.eventos.entities.paymententity.DebitCardEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(DebitCardService.class)
public class DebitCardServiceTest {

    @Autowired
    private DebitCardService debitCardService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<DebitCardEntity> debitCardList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MethodOfPayment");
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            DebitCardEntity entity = factory.manufacturePojo(DebitCardEntity.class);
            entityManager.persist(entity);
            debitCardList.add(entity);
        }
    }

    @Test
    void testCreateDebitCard() throws EntityNotFoundException, IllegalOperationException {
        DebitCardEntity newEntity = factory.manufacturePojo(DebitCardEntity.class);

        DebitCardEntity result = debitCardService.createDebitCard(newEntity);

        assertNotNull(result);

        DebitCardEntity entity = entityManager.find(DebitCardEntity.class, result.getId());

        assertEquals(newEntity.getCardNumber(), entity.getCardNumber());
        assertEquals(newEntity.getCardHolderName(), entity.getCardHolderName());
        assertEquals(newEntity.getSecurityCode(), entity.getSecurityCode());
    }

    @Test
    void testCreateDebitCardWithStoredNumber() {

        assertThrows(IllegalOperationException.class, () -> {

            DebitCardEntity newEntity = factory.manufacturePojo(DebitCardEntity.class);

            newEntity.setCardNumber(debitCardList.get(0).getCardNumber());

            debitCardService.createDebitCard(newEntity);
        });
    }

    @Test
    void testGetDebitCards() {
        List<DebitCardEntity> list = debitCardService.getDebitCards();

        assertEquals(debitCardList.size(), list.size());

        for (DebitCardEntity entity : list) {
            boolean found = false;

            for (DebitCardEntity storedEntity : debitCardList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }

            assertTrue(found);
        }
    }

    @Test
    void testGetDebitCard() throws EntityNotFoundException {
        DebitCardEntity entity = debitCardList.get(0);

        DebitCardEntity resultEntity = debitCardService.getDebitCard(entity.getId());

        assertNotNull(resultEntity);

        assertEquals(resultEntity.getCardNumber(), entity.getCardNumber());
        assertEquals(resultEntity.getCardHolderName(), entity.getCardHolderName());
        assertEquals(resultEntity.getSecurityCode(), entity.getSecurityCode());
    }

    @Test
    void testGetInvalidDebitCard() {

        assertThrows(EntityNotFoundException.class, () -> {
            debitCardService.getDebitCard(0L);
        });
    }

    @Test
    void testUpdateDebitCard() throws EntityNotFoundException, IllegalOperationException {
        DebitCardEntity entity = debitCardList.get(0);

        DebitCardEntity pojoEntity = factory.manufacturePojo(DebitCardEntity.class);

        pojoEntity.setId(entity.getId());

        debitCardService.updateDebitCard(entity.getId(), pojoEntity);

        DebitCardEntity resp = entityManager.find(DebitCardEntity.class, entity.getId());

        assertEquals(pojoEntity.getCardNumber(), resp.getCardNumber());
        assertEquals(pojoEntity.getCardHolderName(), resp.getCardHolderName());
        assertEquals(pojoEntity.getSecurityCode(), resp.getSecurityCode());
    }

    @Test

    void testUpdateDebitCardInvalid() {

        assertThrows(EntityNotFoundException.class, () -> {

            DebitCardEntity pojoEntity = factory.manufacturePojo(DebitCardEntity.class);

            pojoEntity.setId(0L);

            debitCardService.updateDebitCard(0L, pojoEntity);

        });

    }

    @Test
    void testDeleteDebitCard() throws EntityNotFoundException, IllegalOperationException {

        DebitCardEntity entity = debitCardList.get(1);
        debitCardService.deleteDebitCard(entity.getId());

        DebitCardEntity deleted = entityManager.find(DebitCardEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteInvalidDebitCard() {

        assertThrows(EntityNotFoundException.class, () -> {

            debitCardService.deleteDebitCard(0L);

        });

    }
}
