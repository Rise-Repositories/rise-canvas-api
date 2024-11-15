package school.sptech.crudrisecanvas.controllers;

import java.util.HashMap;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Voluntários", description = "Endpoints para gerenciamento de voluntários")
public class VoluntaryController {

    private final VoluntaryService voluntaryService;

    @GetMapping("/{ongId}")
    @Operation(
            summary = "Listar voluntários por ONG",
            description = "Retorna uma lista de voluntários associados a uma ONG identificada pelo ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - Retorna a lista de voluntários"),
                    @ApiResponse(responseCode = "204", description = "Sem conteúdo - Nenhum voluntário encontrado para a ONG fornecida")
            }
    )
    public ResponseEntity<List<VoluntaryOngResponseDto>> getVoluntary(
        @PathVariable Integer ongId,
        @RequestHeader HashMap<String, String> headers
    ){
        String token = headers.get("authorization").substring(7);

        List<VoluntaryOngResponseDto> response = 
            VoluntaryMapper.toOngNoRelationDto(voluntaryService.getVoluntary(ongId, token));

        if(response.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{ongId}")
    @Operation(
            summary = "Criar um novo voluntário",
            description = "Cria um novo voluntário associado à ONG identificada pelo ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Criado - Voluntário criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Solicitação inválida - Dados do voluntário são inválidos")
            }
    )
    public ResponseEntity<VoluntaryOngResponseDto> createVoluntary(
        @RequestBody @Valid VoluntaryRequestDto voluntaryDto,
        @RequestHeader HashMap<String, String> headers,
        @PathVariable Integer ongId
    ){
        String token = headers.get("authorization").substring(7);

        Voluntary voluntary = VoluntaryMapper.toEntity(voluntaryDto);

        VoluntaryOngResponseDto response = 
            VoluntaryMapper.toOngNoRelationDto(voluntaryService.createVoluntary(voluntary, ongId, token));

        return ResponseEntity.status(201).body(response);

    }

    @PostMapping("/{ongId}/{userId}")
    @Operation(
            summary = "Atribuir função a um voluntário",
            description = "Atribui uma função a um voluntário existente na ONG identificada pelo ID da ONG e ID do usuário.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Criado - Função atribuída com sucesso ao voluntário"),
                    @ApiResponse(responseCode = "400", description = "Solicitação inválida - Dados da função são inválidos")
            }
    )
    public ResponseEntity<VoluntaryOngResponseDto> createVoluntary(
        @RequestBody @Valid VoluntaryRoleRequestDto roleDto,
        @RequestHeader HashMap<String, String> headers,
        @PathVariable Integer ongId,
        @PathVariable Integer userId
    ){
        String token = headers.get("authorization").substring(7);


        VoluntaryOngResponseDto response = 
            VoluntaryMapper.toOngNoRelationDto(voluntaryService.createVoluntary(roleDto.getRole(), ongId, userId, token));

        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{id}/role")
    @Operation(
            summary = "Atualizar a função de um voluntário",
            description = "Atualiza a função de um voluntário existente identificado pelo ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - Função atualizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Não encontrado - Voluntário não encontrado")
            }
    )
    public ResponseEntity<VoluntaryOngResponseDto> updateRole(
        @RequestBody VoluntaryRoleRequestDto roleDto,
        @RequestHeader HashMap<String, String> headers,
        @PathVariable Integer id
    ){
        String token = headers.get("authorization").substring(7);

        VoluntaryOngResponseDto response = 
            VoluntaryMapper.toOngNoRelationDto(voluntaryService.updateRole(roleDto.getRole(), id, token));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir um voluntário",
            description = "Exclui um voluntário existente identificado pelo ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Sem conteúdo - Voluntário excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Não encontrado - Voluntário não encontrado")
            }
    )
    public ResponseEntity<Void> deleteVoluntary(
        @RequestHeader HashMap<String, String> headers,
        @PathVariable Integer id
    ){
        String token = headers.get("authorization").substring(7);

        voluntaryService.deleteVoluntary(id, token);

        return ResponseEntity.noContent().build();
    }


    
}
