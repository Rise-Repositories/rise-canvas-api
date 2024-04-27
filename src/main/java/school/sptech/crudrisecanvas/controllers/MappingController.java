package school.sptech.crudrisecanvas.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import school.sptech.crudrisecanvas.utils.Enums.MappingStatus;
import school.sptech.crudrisecanvas.dtos.MappingRequestDto;
import school.sptech.crudrisecanvas.dtos.MappingRequestMapper;
import school.sptech.crudrisecanvas.dtos.MappingResponseDto;
import school.sptech.crudrisecanvas.dtos.MappingResponseMapper;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.repositories.MappingActionRepository;
import school.sptech.crudrisecanvas.repositories.MappingRepository;
import school.sptech.crudrisecanvas.repositories.UserRepositary;

@RestController
@RequestMapping("/mapping")
public class MappingController {

    @Autowired
    MappingActionRepository mappingActionRepository;

    @Autowired
    MappingRepository mappingRepository;

    @Autowired
    UserRepositary userRepositary;

    @GetMapping
    public ResponseEntity<List<MappingResponseDto>> getMappings(){
        List<MappingResponseDto> mappings = MappingResponseMapper.toDto(mappingRepository.findAll());
        if(mappings.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(mappings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MappingResponseDto> getMappingById(@PathVariable Integer id){
        Optional<Mapping> mapping = mappingRepository.findById(id);
        if(mapping.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        MappingResponseDto newMapping = MappingResponseMapper.toDto(mapping.get());

        return ResponseEntity.status(200).body(newMapping);
    }

    @PostMapping
    public ResponseEntity<MappingResponseDto> createMapping(@RequestBody @Valid MappingRequestDto mapping){
        Mapping newMapping = MappingRequestMapper.toEntity(mapping);

        Optional<User> user = userRepositary.findById(1);
        
        if(user.isEmpty()){
            return ResponseEntity.status(404).build();
        }
        
        newMapping.setUsers(List.of(user.get()));
        newMapping.setStatus(MappingStatus.ACTIVE);

        MappingResponseDto response = MappingResponseMapper.toDto(mappingRepository.save(newMapping));

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MappingResponseDto> updateMapping(@PathVariable Integer id, @RequestBody MappingRequestDto mapping){
        Optional<Mapping> mappingToUpdate = mappingRepository.findById(id);
        if(mappingToUpdate.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        Mapping mappingUpdated = mappingToUpdate.get();

        mappingUpdated.setQtyPeople(mapping.getQtyPeople());
        mappingUpdated.setDescription(mapping.getDescription());
        mappingUpdated.setLatitude(mapping.getLatitude());
        mappingUpdated.setLongitude(mapping.getLongitude());

        MappingResponseDto response = MappingResponseMapper.toDto(mappingRepository.save(mappingUpdated));

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMapping(Integer id){
        Optional<Mapping> mapping = mappingRepository.findById(id);
        if(mapping.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        List<Integer> ids = mapping.get().getMappingActions().stream().map(e -> e.getId()).toList();
        mappingActionRepository.deleteAllById(ids);
        mappingRepository.delete(mapping.get());

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/{id}/add-user/{userId}")
    public ResponseEntity<MappingResponseDto> addUser(@PathVariable("id") Integer id,@PathVariable("userId") Integer userId){
        Optional<Mapping> mapping = mappingRepository.findById(id);
        Optional<User> user = userRepositary.findById(userId);

        if(mapping.isEmpty() || user.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        List<User> users = mapping.get().getUsers() == null ? new ArrayList<>() : mapping.get().getUsers();
        users.add(user.get());

        mapping.get().setUsers(users);

        MappingResponseDto response = MappingResponseMapper.toDto(mappingRepository.save(mapping.get()));

        return ResponseEntity.status(200).body(response);
    }
}
