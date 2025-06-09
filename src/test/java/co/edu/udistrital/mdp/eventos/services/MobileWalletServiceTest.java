package co.edu.udistrital.mdp.eventos.services;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.context.annotation.Import;
//
//import co.edu.udistrital.mdp.eventos.entities.paymententity.MobileWalletEntity;
//import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
//import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
//import co.edu.udistrital.mdp.eventos.services.paymententity.MobileWalletService;
//import jakarta.transaction.Transactional;
//import uk.co.jemos.podam.api.PodamFactory;
//import uk.co.jemos.podam.api.PodamFactoryImpl;
//
//@DataJpaTest
//@Transactional
//@Import(MobileWalletService.class)
//public class MobileWalletServiceTest {
//    @Autowired
//    private MobileWalletService mobileWalletService;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    private PodamFactory factory = new PodamFactoryImpl();
//    private List<MobileWalletEntity> mobileWalletList = new ArrayList<>();
//
//    @BeforeEach
//    void setUp() {
//        clearData();
//        insertData();
//    }
//
//    private void clearData() {
//        entityManager.getEntityManager().createQuery("delete from MethodOfPayment");
//    }
//
//    private void insertData() {
//        for (int i = 0; i < 3; i++) {
//            MobileWalletEntity entity = factory.manufacturePojo(MobileWalletEntity.class);
//            entityManager.persist(entity);
//            mobileWalletList.add(entity);
//        }
//    }
//
//    @Test
//    void testCreateMobileWallet() throws EntityNotFoundException, IllegalOperationException {
//        MobileWalletEntity newEntity = factory.manufacturePojo(MobileWalletEntity.class);
//
//        MobileWalletEntity result = mobileWalletService.createMobileWallet(newEntity);
//
//        assertNotNull(result);
//
//        MobileWalletEntity entity = entityManager.find(MobileWalletEntity.class, result.getId());
//
//        assertEquals(newEntity.getPhoneAccount(), entity.getPhoneAccount());
//        assertEquals(newEntity.getTypeOfWallet(), entity.getTypeOfWallet());
//        assertEquals(newEntity.getOtpCode(), entity.getOtpCode());
//        assertEquals(newEntity.getIdentityDocument(), entity.getIdentityDocument());
//        assertEquals(newEntity.getEmail(), entity.getEmail());
//    }
//
//    @Test
//    void testCreateMobileWalletWithStoredPhone() {
//
//        assertThrows(IllegalOperationException.class, () -> {
//
//            MobileWalletEntity newEntity = factory.manufacturePojo(MobileWalletEntity.class);
//
//            newEntity.setPhoneAccount(mobileWalletList.get(0).getPhoneAccount());
//
//            mobileWalletService.createMobileWallet(newEntity);
//        });
//    }
//
//    @Test
//    void testGetMobileWallets() {
//        List<MobileWalletEntity> list = mobileWalletService.getMobileWallets();
//
//        assertEquals(mobileWalletList.size(), list.size());
//
//        for (MobileWalletEntity entity : list) {
//            boolean found = false;
//
//            for (MobileWalletEntity storedEntity : mobileWalletList) {
//                if (entity.getId().equals(storedEntity.getId())) {
//                    found = true;
//                }
//            }
//
//            assertTrue(found);
//        }
//    }
//
//    @Test
//    void testGetMobileWallet() throws EntityNotFoundException {
//        MobileWalletEntity entity = mobileWalletList.get(0);
//
//        MobileWalletEntity resultEntity = mobileWalletService.getMobileWallet(entity.getId());
//
//        assertNotNull(resultEntity);
//
//        assertEquals(resultEntity.getPhoneAccount(), entity.getPhoneAccount());
//        assertEquals(resultEntity.getTypeOfWallet(), entity.getTypeOfWallet());
//        assertEquals(resultEntity.getOtpCode(), entity.getOtpCode());
//        assertEquals(resultEntity.getIdentityDocument(), entity.getIdentityDocument());
//        assertEquals(resultEntity.getEmail(), entity.getEmail());
//    }
//
//    @Test
//    void testGetInvalidMobileWallet() {
//
//        assertThrows(EntityNotFoundException.class, () -> {
//            mobileWalletService.getMobileWallet(0L);
//        });
//    }
//
//    @Test
//    void testUpdateMobileWallet() throws EntityNotFoundException, IllegalOperationException {
//        MobileWalletEntity entity = mobileWalletList.get(0);
//
//        MobileWalletEntity pojoEntity = factory.manufacturePojo(MobileWalletEntity.class);
//
//        pojoEntity.setId(entity.getId());
//
//        mobileWalletService.updateMobileWallet(entity.getId(), pojoEntity);
//
//        MobileWalletEntity resp = entityManager.find(MobileWalletEntity.class, entity.getId());
//
//        assertEquals(pojoEntity.getPhoneAccount(), resp.getPhoneAccount());
//        assertEquals(pojoEntity.getTypeOfWallet(), resp.getTypeOfWallet());
//        //assertEquals(pojoEntity.getOTPCode(), resp.getOTPCode());
//        assertEquals(pojoEntity.getOtpCode(), resp.getOtpCode());
//        assertEquals(pojoEntity.getIdentityDocument(), resp.getIdentityDocument());
//        assertEquals(pojoEntity.getEmail(), resp.getEmail());
//    }
//
//    @Test
//
//    void testUpdateMobileWalletInvalid() {
//
//        assertThrows(EntityNotFoundException.class, () -> {
//
//            MobileWalletEntity pojoEntity = factory.manufacturePojo(MobileWalletEntity.class);
//
//            pojoEntity.setId(0L);
//
//            mobileWalletService.updateMobileWallet(0L, pojoEntity);
//
//        });
//
//    }
//
//    @Test
//    void testUpdateMobileWalletWithDuplicate() {
//        MobileWalletEntity original = mobileWalletList.get(0);
//        MobileWalletEntity duplicateSource = mobileWalletList.get(1);
//
//        MobileWalletEntity updateEntity = factory.manufacturePojo(MobileWalletEntity.class);
//        updateEntity.setId(original.getId());
//        updateEntity.setPhoneAccount(duplicateSource.getPhoneAccount());
//        updateEntity.setTypeOfWallet(duplicateSource.getTypeOfWallet());
//
//        assertThrows(IllegalOperationException.class, () -> {
//            mobileWalletService.updateMobileWallet(original.getId(), updateEntity);
//        });
//    }
//
//    @Test
//    void testDeleteMobileWallet() throws EntityNotFoundException, IllegalOperationException {
//
//        MobileWalletEntity entity = mobileWalletList.get(1);
//        mobileWalletService.deleteMobileWallet(entity.getId());
//
//        MobileWalletEntity deleted = entityManager.find(MobileWalletEntity.class, entity.getId());
//        assertNull(deleted);
//    }
//
//    @Test
//    void testDeleteInvalidMobileWallet() {
//
//        assertThrows(EntityNotFoundException.class, () -> {
//
//            mobileWalletService.deleteMobileWallet(0L);
//
//        });
//
//    }
//}
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

import co.edu.udistrital.mdp.eventos.entities.paymententity.MobileWalletEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.paymententity.MobileWalletService;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MobileWalletService.class)
public class MobileWalletServiceTest {
    @Autowired
    private MobileWalletService mobileWalletService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<MobileWalletEntity> mobileWalletList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("DELETE FROM MethodOfPaymentEntity");
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            MobileWalletEntity entity = factory.manufacturePojo(MobileWalletEntity.class);
            entityManager.persist(entity);
            mobileWalletList.add(entity);
        }
    }

    @Test
    void testCreateMobileWallet() throws EntityNotFoundException, IllegalOperationException {
        MobileWalletEntity newEntity = factory.manufacturePojo(MobileWalletEntity.class);

        MobileWalletEntity result = mobileWalletService.createMobileWallet(newEntity);

        assertNotNull(result);

        MobileWalletEntity entity = entityManager.find(MobileWalletEntity.class, result.getId());

        assertEquals(newEntity.getPhoneAccount(), entity.getPhoneAccount());
        assertEquals(newEntity.getTypeOfWallet(), entity.getTypeOfWallet());
        assertEquals(newEntity.getOtpCode(), entity.getOtpCode());
        assertEquals(newEntity.getIdentityDocument(), entity.getIdentityDocument());
        assertEquals(newEntity.getEmail(), entity.getEmail());
    }

    @Test
    void testCreateMobileWalletWithStoredPhone() {

        assertThrows(IllegalOperationException.class, () -> {

            MobileWalletEntity newEntity = factory.manufacturePojo(MobileWalletEntity.class);

            newEntity.setPhoneAccount(mobileWalletList.get(0).getPhoneAccount());
            newEntity.setTypeOfWallet(mobileWalletList.get(0).getTypeOfWallet());

            mobileWalletService.createMobileWallet(newEntity);
        });
    }

    @Test
    void testGetMobileWallets() {
        List<MobileWalletEntity> list = mobileWalletService.getMobileWallets();

        assertEquals(mobileWalletList.size(), list.size());

        for (MobileWalletEntity entity : list) {
            boolean found = false;

            for (MobileWalletEntity storedEntity : mobileWalletList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }

            assertTrue(found);
        }
    }

    @Test
    void testGetMobileWallet() throws EntityNotFoundException {
        MobileWalletEntity entity = mobileWalletList.get(0);

        MobileWalletEntity resultEntity = mobileWalletService.getMobileWallet(entity.getId());

        assertNotNull(resultEntity);

        assertEquals(resultEntity.getPhoneAccount(), entity.getPhoneAccount());
        assertEquals(resultEntity.getTypeOfWallet(), entity.getTypeOfWallet());
        assertEquals(resultEntity.getOtpCode(), entity.getOtpCode());
        assertEquals(resultEntity.getIdentityDocument(), entity.getIdentityDocument());
        assertEquals(resultEntity.getEmail(), entity.getEmail());
    }

    @Test
    void testGetInvalidMobileWallet() {

        assertThrows(EntityNotFoundException.class, () -> {
            mobileWalletService.getMobileWallet(0L);
        });
    }

    @Test
    void testUpdateMobileWallet() throws EntityNotFoundException, IllegalOperationException {
        MobileWalletEntity entity = mobileWalletList.get(0);

        MobileWalletEntity pojoEntity = factory.manufacturePojo(MobileWalletEntity.class);

        pojoEntity.setId(entity.getId());

        mobileWalletService.updateMobileWallet(entity.getId(), pojoEntity);

        MobileWalletEntity resp = entityManager.find(MobileWalletEntity.class, entity.getId());

        assertEquals(pojoEntity.getPhoneAccount(), resp.getPhoneAccount());
        assertEquals(pojoEntity.getTypeOfWallet(), resp.getTypeOfWallet());
        assertEquals(pojoEntity.getOtpCode(), resp.getOtpCode());
        assertEquals(pojoEntity.getIdentityDocument(), resp.getIdentityDocument());
        assertEquals(pojoEntity.getEmail(), resp.getEmail());
    }

    @Test

    void testUpdateMobileWalletInvalid() {

        assertThrows(EntityNotFoundException.class, () -> {

            MobileWalletEntity pojoEntity = factory.manufacturePojo(MobileWalletEntity.class);

            pojoEntity.setId(0L);

            mobileWalletService.updateMobileWallet(0L, pojoEntity);

        });

    }

    @Test
    void testUpdateMobileWalletWithDuplicate() {
        MobileWalletEntity original = mobileWalletList.get(0);
        MobileWalletEntity duplicateSource = mobileWalletList.get(1);

        MobileWalletEntity updateEntity = factory.manufacturePojo(MobileWalletEntity.class);
        updateEntity.setId(original.getId());
        updateEntity.setPhoneAccount(duplicateSource.getPhoneAccount());
        updateEntity.setTypeOfWallet(duplicateSource.getTypeOfWallet());

        assertThrows(IllegalOperationException.class, () -> {
            mobileWalletService.updateMobileWallet(original.getId(), updateEntity);
        });
    }

    @Test
    void testDeleteMobileWallet() throws EntityNotFoundException, IllegalOperationException {

        MobileWalletEntity entity = mobileWalletList.get(1);
        mobileWalletService.deleteMobileWallet(entity.getId());

        MobileWalletEntity deleted = entityManager.find(MobileWalletEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteInvalidMobileWallet() {

        assertThrows(EntityNotFoundException.class, () -> {

            mobileWalletService.deleteMobileWallet(0L);

        });

    }
}