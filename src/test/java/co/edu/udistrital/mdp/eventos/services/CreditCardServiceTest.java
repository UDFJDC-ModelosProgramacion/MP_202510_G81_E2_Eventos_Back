package co.edu.udistrital.mdp.eventos.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.eventos.entities.paymententity.CreditCardEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.paymententity.CreditCardService;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(CreditCardService.class)
public class CreditCardServiceTest {
    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<CreditCardEntity> creditCardList = new ArrayList<>();

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
            CreditCardEntity entity = factory.manufacturePojo(CreditCardEntity.class);
            entity.setExpirationDate(Date.from(LocalDate.now().plusYears(1)
                    .atStartOfDay(ZoneId.systemDefault()).toInstant()));
            entityManager.persist(entity);
            creditCardList.add(entity);
        }
    }

    @Test
    void testCreateCreditCard() throws EntityNotFoundException, IllegalOperationException {
        CreditCardEntity newEntity = factory.manufacturePojo(CreditCardEntity.class);

        newEntity.setExpirationDate(Date.from(
                LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        CreditCardEntity result = creditCardService.createCreditCard(newEntity);

        assertNotNull(result);

        CreditCardEntity entity = entityManager.find(CreditCardEntity.class, result.getId());

        assertEquals(newEntity.getCardNumber(), entity.getCardNumber());
        assertEquals(newEntity.getCardHolderName(), entity.getCardHolderName());
        assertEquals(newEntity.getExpirationDate(), entity.getExpirationDate());
        assertEquals(newEntity.getSecurityCode(), entity.getSecurityCode());
    }

    @Test
    void testCreateCreditCardWithStoredNumber() {

        assertThrows(IllegalOperationException.class, () -> {

            CreditCardEntity newEntity = factory.manufacturePojo(CreditCardEntity.class);

            newEntity.setExpirationDate(Date.from(
                    LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            newEntity.setCardNumber(creditCardList.get(0).getCardNumber());

            creditCardService.createCreditCard(newEntity);
        });
    }

    @Test
    void testCreateExpiredCreditCard() {
        assertThrows(IllegalOperationException.class, () -> {

            CreditCardEntity newEntity = factory.manufacturePojo(CreditCardEntity.class);

            newEntity.setExpirationDate(Date.from(
                    LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            creditCardService.createCreditCard(newEntity);
        });
    }

    @Test
    void testGetCreditCards() {
        List<CreditCardEntity> list = creditCardService.getCreditCards();

        assertEquals(creditCardList.size(), list.size());

        for (CreditCardEntity entity : list) {
            boolean found = false;

            for (CreditCardEntity storedEntity : creditCardList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }

            assertTrue(found);
        }
    }

    @Test
    void testGetCreditCard() throws EntityNotFoundException {
        CreditCardEntity entity = creditCardList.get(0);

        CreditCardEntity resultEntity = creditCardService.getCreditCard(entity.getId());

        assertNotNull(resultEntity);

        assertEquals(resultEntity.getCardNumber(), entity.getCardNumber());
        assertEquals(resultEntity.getCardHolderName(), entity.getCardHolderName());
        assertEquals(resultEntity.getExpirationDate(), entity.getExpirationDate());
        assertEquals(resultEntity.getSecurityCode(), entity.getSecurityCode());
    }

    @Test
    void testGetInvalidCreditCard() {

        assertThrows(EntityNotFoundException.class, () -> {
            creditCardService.getCreditCard(0L);
        });
    }

    @Test
    void testUpdateCreditCard() throws EntityNotFoundException, IllegalOperationException {
        CreditCardEntity entity = creditCardList.get(0);

        CreditCardEntity pojoEntity = factory.manufacturePojo(CreditCardEntity.class);

        // Aseguramos que no cambie el ID
        pojoEntity.setId(entity.getId());

        // Fecha válida (para no lanzar excepción)
        pojoEntity.setExpirationDate(Date.from(
                LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        creditCardService.updateCreditCard(entity.getId(), pojoEntity);

        CreditCardEntity resp = entityManager.find(CreditCardEntity.class, entity.getId());

        assertEquals(pojoEntity.getCardNumber(), resp.getCardNumber());
        assertEquals(pojoEntity.getCardHolderName(), resp.getCardHolderName());
        assertEquals(pojoEntity.getExpirationDate(), resp.getExpirationDate());
        assertEquals(pojoEntity.getSecurityCode(), resp.getSecurityCode());
    }

    @Test

    void testUpdateCreditCardInvalid() {

        assertThrows(EntityNotFoundException.class, () -> {

            CreditCardEntity pojoEntity = factory.manufacturePojo(CreditCardEntity.class);

            pojoEntity.setId(0L);

            creditCardService.updateCreditCard(0L, pojoEntity);

        });

    }

    @Test
    void testUpdateExpiredCreditCard() {
        assertThrows(IllegalOperationException.class, () -> {

            CreditCardEntity entity = creditCardList.get(0);
            CreditCardEntity pojoEntity = factory.manufacturePojo(CreditCardEntity.class);

            pojoEntity.setExpirationDate(Date.from(
                    LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            pojoEntity.setId(entity.getId());
            creditCardService.updateCreditCard(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testDeleteCreditCard() throws EntityNotFoundException, IllegalOperationException {

        CreditCardEntity entity = creditCardList.get(1);
        creditCardService.deleteCreditCard(entity.getId());

        CreditCardEntity deleted = entityManager.find(CreditCardEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteInvalidCreditCard() {

        assertThrows(EntityNotFoundException.class, () -> {

            creditCardService.deleteCreditCard(0L);

        });

    }
}
