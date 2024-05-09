package school.sptech.crudrisecanvas.controllers;

import java.util.List;

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
import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.dtos.mapping.MappingRequestDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingRequestMapper;
import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseMapper;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.service.MappingService;

@RestController
@RequestMapping("/mapping")
@RequiredArgsConstructor
public class MappingController {
    private final MappingService mappingService;

    @GetMapping
    public ResponseEntity<List<MappingResponseDto>> getMappings(){
        List<MappingResponseDto> mappings = MappingResponseMapper.toDto(mappingService.getMappings());
        if(mappings.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(mappings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MappingResponseDto> getMappingById(@PathVariable Integer id){
        Mapping mapping = mappingService.getMappingById(id);

        MappingResponseDto response = MappingResponseMapper.toDto(mapping);

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping
    public ResponseEntity<MappingResponseDto> createMapping(@RequestBody @Valid MappingRequestDto mappingDto){
        Mapping mapping = MappingRequestMapper.toEntity(mappingDto);

        MappingResponseDto response = MappingResponseMapper.toDto(mappingService.createMapping(mapping));

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MappingResponseDto> updateMapping(@PathVariable Integer id, @RequestBody MappingRequestDto mappingDto){
        Mapping mapping = MappingRequestMapper.toEntity(mappingDto);

        MappingResponseDto response = MappingResponseMapper.toDto(
            mappingService.updateMapping(id, mapping)
        );

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMapping(Integer id){
        mappingService.deleteMapping(id);
        return ResponseEntity.status(204).build();
    }

    @PostMapping("/{id}/add-user/{userId}")
    public ResponseEntity<MappingResponseDto> addUser(@PathVariable("id") Integer id,@PathVariable("userId") Integer userId){
        MappingResponseDto response = MappingResponseMapper.toDto(
            mappingService.addUser(id, userId)
        );

        return ResponseEntity.status(200).body(response);
    }
}
