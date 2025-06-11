package co.edu.udistrital.mdp.eventos.controllers.eventcontrollers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.eventos.dto.eventdto.LocationDTO;
import co.edu.udistrital.mdp.eventos.entities.evententity.LocationEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.services.evententity.LocationService;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LocationDTO> findAll() {
        List<LocationEntity> locations = locationService.getAllLocations();
        return modelMapper.map(locations, new TypeToken<List<LocationDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LocationDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        LocationEntity locationEntity = locationService.getLocationById(id);
        return modelMapper.map(locationEntity, LocationDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDTO create(@RequestBody LocationDTO locationDTO) throws EntityNotFoundException {
        LocationEntity locationEntity = locationService.createLocation(modelMapper.map(locationDTO, LocationEntity.class));
        return modelMapper.map(locationEntity, LocationDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LocationDTO update(@PathVariable Long id, @RequestBody LocationDTO locationDTO) throws EntityNotFoundException {
        LocationEntity locationEntity = locationService.updateLocation(id, modelMapper.map(locationDTO, LocationEntity.class));
        return modelMapper.map(locationEntity, LocationDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        locationService.deleteLocation(id);
    }
}