package school.sptech.crudrisecanvas.controllers;

import java.util.List;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.dtos.address.AddressMapper;
import school.sptech.crudrisecanvas.dtos.mapping.MappingRequestDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingMapper;
import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseDto;
import school.sptech.crudrisecanvas.entities.Address;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.service.MappingService;

@RestController
@RequestMapping("/mapping")
@RequiredArgsConstructor
public class MappingController {
    private final MappingService mappingService;

    @GetMapping
    public ResponseEntity<List<MappingResponseDto>> getMappings(){
        List<MappingResponseDto> mappings = MappingMapper.toResponse(mappingService.getMappings());
        if(mappings.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(mappings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MappingResponseDto> getMappingById(@PathVariable Integer id){
        Mapping mapping = mappingService.getMappingById(id);

        MappingResponseDto response = MappingMapper.toResponse(mapping);

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping
    public ResponseEntity<MappingResponseDto> createMapping(@RequestBody @Valid MappingRequestDto mappingDto, @RequestHeader HashMap<String,String> headers){
        Mapping mapping = MappingMapper.toEntity(mappingDto);
        MappingResponseDto response = MappingMapper.toResponse(
            mappingService.createMapping(mapping, headers.get("authorization").substring(7))
        );

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MappingResponseDto> updateMapping(@PathVariable Integer id, @RequestBody MappingRequestDto mappingDto){
        Mapping mapping = MappingMapper.toEntity(mappingDto);

        MappingResponseDto response = MappingMapper.toResponse(
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
        MappingResponseDto response = MappingMapper.toResponse(
            mappingService.addUser(id, userId)
        );

        return ResponseEntity.status(200).body(response);
    }
}
