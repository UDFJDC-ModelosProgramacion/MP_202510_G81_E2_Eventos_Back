package co.edu.udistrital.mdp.eventos.services;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.LocationEntity;
import co.edu.udistrital.mdp.eventos.services.evententity.LocationService;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import jakarta.transaction.Transactional;

@DataJpaTest
@Transactional
@Import(LocationService.class)
public class LocationServiceTest {

   @Autowired
   private LocationService locationService;

   @Autowired
   private TestEntityManager entityManager;

   private PodamFactory factory = new PodamFactoryImpl();
   private LocationEntity sampleLocation;
   private EventEntity sampleEvent;

   @BeforeEach
    void setUp() {
        // Limpiar datos primero
        entityManager.getEntityManager().createQuery("DELETE FROM LocationEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM EventEntity").executeUpdate();

        // Luego crear nuevos datos
        sampleEvent = factory.manufacturePojo(EventEntity.class);
        entityManager.persist(sampleEvent);

        sampleLocation = factory.manufacturePojo(LocationEntity.class);
        sampleLocation.setEvent(sampleEvent);
        entityManager.persist(sampleLocation);
    }

   @Test
    void testCreateLocation() throws EntityNotFoundException {
        // Crear un nuevo evento para esta prueba específica
        EventEntity newEvent = factory.manufacturePojo(EventEntity.class);
        entityManager.persist(newEvent);

        LocationEntity newLocation = factory.manufacturePojo(LocationEntity.class);
        newLocation.setEvent(newEvent); // Usar el nuevo evento, no el sampleEvent

        LocationEntity result = locationService.createLocation(newLocation);
        assertNotNull(result.getId());
        assertEquals(newLocation.getName(), result.getName());
    }

   @Test
   void testCreateLocationWithoutAddress() {
       LocationEntity newLocation = factory.manufacturePojo(LocationEntity.class);
       newLocation.setLocation(null);

       assertThrows(IllegalArgumentException.class, () -> locationService.createLocation(newLocation));
   }

   @Test
   void testGetLocationById() throws EntityNotFoundException {
       LocationEntity found = locationService.getLocationById(sampleLocation.getId());
       assertEquals(sampleLocation.getId(), found.getId());
   }

   @Test
   void testUpdateLocationCapacity() throws EntityNotFoundException {
       LocationEntity updates = new LocationEntity();
       updates.setCapacity(100);
       
       LocationEntity updated = locationService.updateLocation(sampleLocation.getId(), updates);
       assertEquals(100, updated.getCapacity());
   }

   @Test
   void testGetLocationsByEvent() {
       List<LocationEntity> locations = locationService.getLocationsByEvent(sampleEvent.getId());
       assertFalse(locations.isEmpty());
       assertEquals(sampleLocation.getId(), locations.get(0).getId());
   }
}
