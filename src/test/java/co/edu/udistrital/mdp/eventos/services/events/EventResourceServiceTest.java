package co.edu.udistrital.mdp.eventos.services.events;

import co.edu.udistrital.mdp.eventos.services.evententity.EventResourceService;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.eventos.entities.evententity.EventEntity;
import co.edu.udistrital.mdp.eventos.entities.evententity.ResourceEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import jakarta.transaction.Transactional;

import java.util.List;

@DataJpaTest
@Transactional
@Import(EventResourceService.class)
public class EventResourceServiceTest {

    @Autowired
    private EventResourceService eventResourceService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private EventEntity sampleEvent;
    private ResourceEntity sampleResource;
    private ResourceEntity secondResource;

    @BeforeEach
    void setUp() {
        sampleEvent = factory.manufacturePojo(EventEntity.class);
        entityManager.persist(sampleEvent);

        sampleResource = factory.manufacturePojo(ResourceEntity.class);
        entityManager.persist(sampleResource);

        secondResource = factory.manufacturePojo(ResourceEntity.class);
        entityManager.persist(secondResource);
    }

    @Test
    void testAddResourceToEvent() throws EntityNotFoundException {
        ResourceEntity result = eventResourceService.addResourceToEvent(sampleEvent.getId(), sampleResource.getId());
        
        assertNotNull(result);
        assertEquals(sampleResource.getId(), result.getId());
        assertEquals(sampleEvent.getId(), result.getEvent().getId());
    }

    @Test
    void testGetEventResources() throws EntityNotFoundException {
        eventResourceService.addResourceToEvent(sampleEvent.getId(), sampleResource.getId());
        
        List<ResourceEntity> resources = eventResourceService.getEventResources(sampleEvent.getId());
        assertFalse(resources.isEmpty());
        assertEquals(sampleResource.getId(), resources.get(0).getId());
    }

    @Test
    void testGetEventResource() throws EntityNotFoundException, IllegalOperationException {
        eventResourceService.addResourceToEvent(sampleEvent.getId(), sampleResource.getId());
        
        ResourceEntity found = eventResourceService.getEventResource(sampleEvent.getId(), sampleResource.getId());
        assertEquals(sampleResource.getId(), found.getId());
    }

    @Test
    void testGetNonAssociatedResource() {
        assertThrows(IllegalOperationException.class, 
            () -> eventResourceService.getEventResource(sampleEvent.getId(), sampleResource.getId()));
    }

    @Test
    void testRemoveResourceFromEvent() throws EntityNotFoundException, IllegalOperationException {
        eventResourceService.addResourceToEvent(sampleEvent.getId(), sampleResource.getId());
        
        eventResourceService.removeResourceFromEvent(sampleEvent.getId(), sampleResource.getId());
        
        EventEntity updatedEvent = entityManager.find(EventEntity.class, sampleEvent.getId());
        assertTrue(updatedEvent.getResource().isEmpty());
    }

    @Test
    void testReplaceEventResources() throws EntityNotFoundException {
        eventResourceService.addResourceToEvent(sampleEvent.getId(), sampleResource.getId());
        
        List<ResourceEntity> newResources = List.of(secondResource);
        List<ResourceEntity> result = eventResourceService.replaceEventResources(sampleEvent.getId(), newResources);
        
        assertEquals(1, result.size());
        assertEquals(secondResource.getId(), result.get(0).getId());
        
        EventEntity updatedEvent = entityManager.find(EventEntity.class, sampleEvent.getId());
        assertEquals(1, updatedEvent.getResource().size());
    }

    @Test
    void testAddResourceToNonExistingEvent() {
        assertThrows(EntityNotFoundException.class, 
            () -> eventResourceService.addResourceToEvent(999L, sampleResource.getId()));
    }

    @Test
    void testAddNonExistingResourceToEvent() {
        assertThrows(EntityNotFoundException.class, 
            () -> eventResourceService.addResourceToEvent(sampleEvent.getId(), 999L));
    }
}