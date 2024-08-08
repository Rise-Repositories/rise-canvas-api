package school.sptech.crudrisecanvas.controllers;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryMapper;
import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryOngResponseDto;
import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryRequestDto;
import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryRoleRequestDto;
import school.sptech.crudrisecanvas.entities.Voluntary;
import school.sptech.crudrisecanvas.service.VoluntaryService;

@RestController
@RequestMapping("/voluntary")
@RequiredArgsConstructor
public class VoluntaryController {

    private final VoluntaryService voluntaryService;

    @GetMapping("/{ongId}")
    public ResponseEntity<List<VoluntaryOngResponseDto>> getVoluntary(
        @PathVariable Integer ongId,
        @RequestHeader HashMap<String, String> headers
    ){
        String token = headers.get("Authorization").substring(7);

        List<VoluntaryOngResponseDto> response = 
            VoluntaryMapper.toOngNoRelationDto(voluntaryService.getVoluntary(ongId, token));

        if(response.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{ongId}")
    public ResponseEntity<VoluntaryOngResponseDto> createVoluntary(
        @RequestBody @Valid VoluntaryRequestDto voluntaryDto,
        @RequestHeader HashMap<String, String> headers,
        @PathVariable Integer ongId
    ){
        String token = headers.get("Authorization").substring(7);

        Voluntary voluntary = VoluntaryMapper.toEntity(voluntaryDto);

        VoluntaryOngResponseDto response = 
            VoluntaryMapper.toOngNoRelationDto(voluntaryService.createVoluntary(voluntary, ongId, token));

        return ResponseEntity.status(201).body(response);

    }

    @PostMapping("/{ongId}/{userId}")
    public ResponseEntity<VoluntaryOngResponseDto> createVoluntary(
        @RequestBody @Valid VoluntaryRoleRequestDto roleDto,
        @RequestHeader HashMap<String, String> headers,
        @PathVariable Integer ongId,
        @PathVariable Integer userId
    ){
        String token = headers.get("Authorization").substring(7);


        VoluntaryOngResponseDto response = 
            VoluntaryMapper.toOngNoRelationDto(voluntaryService.createVoluntary(roleDto.getRole(), ongId, userId, token));

        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<VoluntaryOngResponseDto> updateRole(
        @RequestBody VoluntaryRoleRequestDto roleDto,
        @RequestHeader HashMap<String, String> headers,
        @PathVariable Integer id
    ){
        String token = headers.get("Authorization").substring(7);

        VoluntaryOngResponseDto response = 
            VoluntaryMapper.toOngNoRelationDto(voluntaryService.updateRole(roleDto.getRole(), id, token));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoluntary(
        @RequestHeader HashMap<String, String> headers,
        @PathVariable Integer id
    ){
        String token = headers.get("Authorization").substring(7);

        voluntaryService.deleteVoluntary(id, token);

        return ResponseEntity.noContent().build();
    }


    
}
