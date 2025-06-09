package co.edu.udistrital.mdp.eventos.services;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.ResourceEntity;
import co.edu.udistrital.mdp.eventos.services.evententity.ResourceService;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import jakarta.transaction.Transactional;

@DataJpaTest
@Transactional
@Import(ResourceService.class)
public class ResourceServiceTest {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private ResourceEntity sampleResource;
    private EventEntity sampleEvent;

    @BeforeEach
    void setUp() {
        sampleEvent = factory.manufacturePojo(EventEntity.class);
        entityManager.persist(sampleEvent);

        sampleResource = factory.manufacturePojo(ResourceEntity.class);
        sampleResource.setEvent(sampleEvent);
        entityManager.persist(sampleResource);
    }

    @Test
    void testCreateResource() throws EntityNotFoundException {
        ResourceEntity newResource = factory.manufacturePojo(ResourceEntity.class);
        newResource.setEvent(sampleEvent);

        ResourceEntity result = resourceService.createResource(newResource);
        assertNotNull(result.getId());
        assertEquals(newResource.getUrl(), result.getUrl());
    }

    @Test
    void testCreateResourceWithoutType() {
        ResourceEntity newResource = factory.manufacturePojo(ResourceEntity.class);
        newResource.setType(null);

        assertThrows(IllegalArgumentException.class, () -> resourceService.createResource(newResource));
    }

    @Test
    void testGetResourceById() throws EntityNotFoundException {
        ResourceEntity found = resourceService.getResourceById(sampleResource.getId());
        assertEquals(sampleResource.getId(), found.getId());
    }

    @Test
    void testUpdateResourceUrl() throws EntityNotFoundException {
        ResourceEntity updates = new ResourceEntity();
        updates.setUrl("https://new-url.com");
        
        ResourceEntity updated = resourceService.updateResource(sampleResource.getId(), updates);
        assertEquals("https://new-url.com", updated.getUrl());
    }

    @Test
    void testGetResourcesByEvent() {
        List<ResourceEntity> resources = resourceService.getResourcesByEvent(sampleEvent.getId());
        assertFalse(resources.isEmpty());
        assertEquals(sampleResource.getId(), resources.get(0).getId());
    }
}