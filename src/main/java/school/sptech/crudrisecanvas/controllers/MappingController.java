package school.sptech.crudrisecanvas.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import school.sptech.crudrisecanvas.Utils.Enums.MappingStatus;
import school.sptech.crudrisecanvas.dtos.MappingRequestDto;
import school.sptech.crudrisecanvas.dtos.MappingRequestMapper;
import school.sptech.crudrisecanvas.dtos.MappingResponseDto;
import school.sptech.crudrisecanvas.dtos.MappingResponseMapper;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.repositories.MappingRepository;
import school.sptech.crudrisecanvas.repositories.UserRepositary;

@RestController
@RequestMapping("/mapping")
public class MappingController {

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
    public ResponseEntity<MappingResponseDto> getMappingById(Integer id){
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
        
        newMapping.setUser(user.get());
        newMapping.setStatus(MappingStatus.ACTIVE);

        MappingResponseDto response = MappingResponseMapper.toDto(mappingRepository.save(newMapping));

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MappingResponseDto> updateMapping(Integer id, MappingRequestDto mapping){
        Optional<Mapping> mappingToUpdate = mappingRepository.findById(id);
        if(mappingToUpdate.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        Mapping mappingUpdated = MappingRequestMapper.toEntity(mapping);
        mappingUpdated.setId(id);

        MappingResponseDto response = MappingResponseMapper.toDto(mappingRepository.save(mappingUpdated));

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMapping(Integer id){
        Optional<Mapping> mapping = mappingRepository.findById(id);
        if(mapping.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        mappingRepository.delete(mapping.get());

        return ResponseEntity.status(204).build();
    }
}
