package co.edu.udistrital.mdp.eventos.controllers.eventcontrollers;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.eventos.dto.eventdto.ResourceDTO;
import co.edu.udistrital.mdp.eventos.entities.evententity.ResourceEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.evententity.ResourceService;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResourceDTO> findAll() {
        List<ResourceEntity> resources = resourceService.getAllResources();
        return modelMapper.map(resources, new TypeToken<List<ResourceDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResourceDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        ResourceEntity resourceEntity = resourceService.getResourceById(id);
        return modelMapper.map(resourceEntity, ResourceDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceDTO create(@RequestBody ResourceDTO resourceDTO) throws EntityNotFoundException {
        ResourceEntity resourceEntity = resourceService.createResource(modelMapper.map(resourceDTO, ResourceEntity.class));
        return modelMapper.map(resourceEntity, ResourceDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResourceDTO update(@PathVariable Long id, @RequestBody ResourceDTO resourceDTO) throws EntityNotFoundException {
        ResourceEntity resourceEntity = resourceService.updateResource(id, modelMapper.map(resourceDTO, ResourceEntity.class));
        return modelMapper.map(resourceEntity, ResourceDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        resourceService.deleteResource(id);
    }
}