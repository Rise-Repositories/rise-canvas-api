package school.sptech.crudrisecanvas.controllers;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import school.sptech.crudrisecanvas.dtos.ong.OngRequestDto;
import school.sptech.crudrisecanvas.dtos.ong.OngRequestMapper;
import school.sptech.crudrisecanvas.dtos.ong.OngResponseDto;
import school.sptech.crudrisecanvas.dtos.ong.OngResponseMapper;
import school.sptech.crudrisecanvas.dtos.ong.OngUpdateDto;
import school.sptech.crudrisecanvas.dtos.user.UserResponseMapper;
import school.sptech.crudrisecanvas.entities.Ong;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.service.OngService;

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

        List<OngResponseDto> response = OngResponseMapper.toDto(ongs);

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

        OngResponseDto result = OngResponseMapper.toDto(ong);

        return ResponseEntity.status(200).body(result);
    }

    @PostMapping("/register")
    @Operation(summary = "Criar uma nova ONG")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado - Retorna os detalhes da nova ONG"),
            @ApiResponse(responseCode = "409", description = "Conflito - O email, CNPJ ou CPF já está em uso")
    })
    public ResponseEntity<OngResponseDto> createOng(@RequestBody @Valid OngRequestDto ongDto) {
        Ong ong = OngRequestMapper.toEntity(ongDto);
        /*
         * TODO:
         * Precisa usar dentro da ong o UserRequestDto
         * fica muito feio usando get...User()
         * e melhor usar ong.getUser()
         * ai mata esse mapper
         */
        User user = UserResponseMapper.toEntity(ongDto);

        OngResponseDto result = OngResponseMapper.toDto(
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
    public ResponseEntity<OngResponseDto> updateOng(@PathVariable int id,@RequestBody @Valid OngUpdateDto ongDto) {
        Ong ong = OngRequestMapper.toEntity(ongDto);

        OngResponseDto result = OngResponseMapper.toDto(ongService.updateOng(id, ong));

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
}