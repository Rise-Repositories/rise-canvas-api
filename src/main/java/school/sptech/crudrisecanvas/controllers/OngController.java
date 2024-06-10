package school.sptech.crudrisecanvas.controllers;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.dtos.ong.*;
import school.sptech.crudrisecanvas.dtos.user.UserMapper;
import school.sptech.crudrisecanvas.entities.Ong;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.service.OngService;
import school.sptech.crudrisecanvas.utils.Enums.OngStatus;

@RestController
@RequestMapping("/ong")
@RequiredArgsConstructor
public class OngController {
    
    private final OngService ongService;

    @GetMapping
    @Operation(summary = "Listar todas as ONGs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna a lista de ONGs"),
            @ApiResponse(responseCode = "204", description = "Sem conteúdo - Não há ONGs cadastradas")
    })
    public ResponseEntity<List<OngResponseDto>> getOngs() {
        List<Ong> ongs = ongService.getOngs();

        if(ongs.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<OngResponseDto> response = OngMapper.toResponse(ongs);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes de uma ONG pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna os detalhes da ONG"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - ONG não encontrada")
    })
    public ResponseEntity<OngResponseDto> getOng(@PathVariable int id) {
        Ong ong = ongService.getOngById(id);

        OngResponseDto result = OngMapper.toResponse(ong);

        return ResponseEntity.status(200).body(result);
    }

    @PostMapping
    @Operation(summary = "Criar uma nova ONG")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado - Retorna os detalhes da nova ONG"),
            @ApiResponse(responseCode = "409", description = "Conflito - O email, CNPJ ou CPF já está em uso")
    })
    public ResponseEntity<OngResponseDto> createOng(@RequestBody @Valid OngRequestDto ongDto) {
        Ong ong = OngMapper.toEntity(ongDto);
        User user = UserMapper.toEntity(ongDto.getUser());

        OngResponseDto result = OngMapper.toResponse(
            ongService.createOng(ong, user)
        );
        result.setActions(new ArrayList<>());

        return ResponseEntity.status(201).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma ONG existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna os detalhes da ONG atualizada"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - ONG não encontrada")
    })
    public ResponseEntity<OngResponseDto> updateOng(@PathVariable int id,@RequestBody @Valid OngRequestUpdateDto ongDto) {
        Ong ong = OngMapper.toEntity(ongDto);

        OngResponseDto result = OngMapper.toResponse(ongService.updateOng(id, ong));

        return ResponseEntity.status(200).body(result);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma ONG pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sem conteúdo - ONG excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - ONG não encontrada")
    })
    public ResponseEntity<Void> deleteOng(@PathVariable int id) {
        ongService.deleteOng(id);
        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar o status de uma ONG pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna os detalhes da ONG atualizada"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - ONG não encontrada")
    })
    public ResponseEntity<OngResponseDto> patchStatusOng(@PathVariable int id, @RequestBody @Valid OngPatchStatusDto ongDto) {
        OngStatus ongStatus = ongDto.getStatus();

        OngResponseDto result = OngMapper.toResponse(ongService.changeStatus(id, ongStatus));

        return ResponseEntity.status(200).body(result);
    }
}