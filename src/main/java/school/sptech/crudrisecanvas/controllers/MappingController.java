package school.sptech.crudrisecanvas.controllers;

import java.util.List;
import java.util.HashMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.dtos.address.AddressMapper;
import school.sptech.crudrisecanvas.dtos.mapping.MappingRequestDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingMapper;
import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseDto;
import school.sptech.crudrisecanvas.entities.Address;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.service.MappingService;
import school.sptech.crudrisecanvas.utils.Coordinates;

@RestController
@RequestMapping("/mapping")
@RequiredArgsConstructor
@Tag(name = "Mapeamentos", description = "Endpoints para gerenciamento de mapeamentos")
public class MappingController {
    private final MappingService mappingService;

    @GetMapping
    @Operation(
            summary = "Obter todos os mapeamentos",
            description = "Retorna uma lista de todos os mapeamentos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de mapeamentos retornada com sucesso"),
                    @ApiResponse(responseCode = "204", description = "Nenhum mapeamento encontrado")
            }
    )
    public ResponseEntity<List<MappingResponseDto>> getMappings(){
        List<MappingResponseDto> mappings = MappingMapper.toResponse(mappingService.getMappings());
        if(mappings.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(mappings);
    }

    @GetMapping("/by-coordinates")
    @Operation(
            summary = "Obter mapeamentos por coordenadas",
            description = "Retorna uma lista de mapeamentos baseados nas coordenadas e raio fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de mapeamentos retornada com sucesso"),
                    @ApiResponse(responseCode = "204", description = "Nenhum mapeamento encontrado")
            }
    )
    public ResponseEntity<List<MappingResponseDto>> getMappingsByCoordinates(@RequestParam("coordinates") String coordinates, @RequestParam("radius") Double radius){

        Coordinates coords = new Coordinates(coordinates);

        List<MappingResponseDto> mappings = MappingMapper.toResponse(mappingService.getMappingsByCoordinates(coords, radius));

        if(mappings.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(mappings);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obter mapeamento por ID",
            description = "Retorna um mapeamento específico baseado no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mapeamento encontrado e retornado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Mapeamento não encontrado")
            }
    )
    public ResponseEntity<MappingResponseDto> getMappingById(@PathVariable Integer id){
        Mapping mapping = mappingService.getMappingById(id);

        MappingResponseDto response = MappingMapper.toResponse(mapping);

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping
    @Operation(
            summary = "Criar um novo mapeamento",
            description = "Cria um novo mapeamento com os dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Mapeamento criado com sucesso")
            }
    )
    public ResponseEntity<MappingResponseDto> createMapping(@RequestBody @Valid MappingRequestDto mappingDto, @RequestHeader HashMap<String,String> headers){
        Mapping mapping = MappingMapper.toEntity(mappingDto);
        MappingResponseDto response = MappingMapper.toResponse(
            mappingService.createMapping(mapping, headers.get("authorization").substring(7))
        );

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar um mapeamento existente",
            description = "Atualiza os dados de um mapeamento existente baseado no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mapeamento atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Mapeamento não encontrado")
            }
    )
    public ResponseEntity<MappingResponseDto> updateMapping(@PathVariable Integer id, @RequestBody MappingRequestDto mappingDto){
        Mapping mapping = MappingMapper.toEntity(mappingDto);

        MappingResponseDto response = MappingMapper.toResponse(
            mappingService.updateMapping(id, mapping)
        );

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir um mapeamento por ID",
            description = "Exclui um mapeamento específico baseado no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Mapeamento excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Mapeamento não encontrado")
            }
    )
    public ResponseEntity<Void> deleteMapping(Integer id){
        mappingService.deleteMapping(id);
        return ResponseEntity.status(204).build();
    }

    @PostMapping("/{id}/add-user/{userId}")
    @Operation(
            summary = "Adicionar um usuário a um mapeamento",
            description = "Adiciona um usuário específico a um mapeamento baseado no ID do mapeamento e ID do usuário.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário adicionado ao mapeamento com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Mapeamento ou usuário não encontrado")
            }
    )
    public ResponseEntity<MappingResponseDto> addUser(@PathVariable("id") Integer id,@PathVariable("userId") Integer userId){
        MappingResponseDto response = MappingMapper.toResponse(
            mappingService.addUser(id, userId)
        );

        return ResponseEntity.status(200).body(response);
    }
}
