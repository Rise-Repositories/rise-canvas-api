package school.sptech.crudrisecanvas.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import school.sptech.crudrisecanvas.utils.Enums.OngStatus;
import school.sptech.crudrisecanvas.dtos.OngRequestDto;
import school.sptech.crudrisecanvas.dtos.OngRequestMapper;
import school.sptech.crudrisecanvas.dtos.OngResponseDto;
import school.sptech.crudrisecanvas.dtos.OngResponseMapper;
import school.sptech.crudrisecanvas.entities.Ong;
import school.sptech.crudrisecanvas.repositories.OngRepository;

@RestController
@RequestMapping("/ong")

public class OngController {
    @Autowired
    OngRepository ongRepository;

    @GetMapping
    @Operation(summary = "Listar todas as ONGs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna a lista de ONGs"),
            @ApiResponse(responseCode = "204", description = "Sem conteúdo - Não há ONGs cadastradas")
    })
    public ResponseEntity<List<OngResponseDto>> getOngs() {
        List<Ong> ongs = ongRepository.findAll();
        if(ongs.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<OngResponseDto> result = OngResponseMapper.toDto(ongs);

        return ResponseEntity.status(200).body(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes de uma ONG pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna os detalhes da ONG"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - ONG não encontrada")
    })
    public ResponseEntity<OngResponseDto> getOng(@PathVariable int id) {
        Optional<Ong> ong = ongRepository.findById(id);
        if(ong.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        OngResponseDto result = OngResponseMapper.toDto(ong.get());

        return ResponseEntity.status(200).body(result);
    }

    @PostMapping
    @Operation(summary = "Criar uma nova ONG")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado - Retorna os detalhes da nova ONG"),
            @ApiResponse(responseCode = "409", description = "Conflito - O email ou CNPJ já está em uso")
    })
    public ResponseEntity<OngResponseDto> createOng(@RequestBody @Valid OngRequestDto ong) {
        if(ongRepository.countWithCnpj(ong.getCnpj()) > 0){
            return ResponseEntity.status(409).build();
        }

        Ong ongEntity = OngRequestMapper.toEntity(ong);
        ongEntity.setStatus(OngStatus.PENDING);

        OngResponseDto result = OngResponseMapper.toDto(ongRepository.save(ongEntity));
        result.setActions(new ArrayList<>());

        return ResponseEntity.status(201).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma ONG existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna os detalhes da ONG atualizada"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - ONG não encontrada")
    })
    public ResponseEntity<OngResponseDto> updateOng(@PathVariable int id,@RequestBody @Valid OngRequestDto ong) {
        Optional<Ong> ongOptional = ongRepository.findById(id);
        if(ongOptional.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        Ong ongEntity = ongOptional.get();

        ongEntity.setName(ong.getName());
        ongEntity.setCnpj(ong.getCnpj());
        ongEntity.setCep(ong.getCep());
        ongEntity.setDescription(ong.getDescription());
        ongEntity.setAddress(ong.getAddress());

        OngResponseDto result = OngResponseMapper.toDto(ongRepository.save(ongEntity));

        return ResponseEntity.status(200).body(result);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma ONG pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sem conteúdo - ONG excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - ONG não encontrada")
    })
    public ResponseEntity<Void> deleteOng(@PathVariable int id) {
        Optional<Ong> ong = ongRepository.findById(id);
        if(ong.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        ongRepository.deleteById(id);
        return ResponseEntity.status(204).build();
    }
}