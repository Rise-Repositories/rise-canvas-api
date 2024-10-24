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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.dtos.action.ActionRequestDto;
import school.sptech.crudrisecanvas.dtos.action.ActionMapper;
import school.sptech.crudrisecanvas.dtos.action.ActionResponseDto;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionRequestDto;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionResponseDto;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionMapper;
import school.sptech.crudrisecanvas.entities.Action;
import school.sptech.crudrisecanvas.entities.MappingAction;
import school.sptech.crudrisecanvas.service.ActionService;
import school.sptech.crudrisecanvas.utils.Coordinates;

@RestController
@RequestMapping("/actions")
@RequiredArgsConstructor
@Tag(name = "Ações", description = "Endpoints para gerenciamento de ações")
public class ActionController {

    private final ActionService actionService;

    @GetMapping
    @Operation(summary = "Obter todas as ações", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de ações"),
            @ApiResponse(responseCode = "204", description = "Nenhuma ação encontrada")
    })
    public ResponseEntity<List<ActionResponseDto>> getActions(){
        List<Action> actions = actionService.getAll();

        if(actions.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        List<ActionResponseDto> actionsResponse = ActionMapper.toResponse(actions);
        return ResponseEntity.status(200).body(actionsResponse);
    }

    @GetMapping("/by-coordinates")
    @Operation(summary = "Obter todas as ações por coordenadas", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de ações"),
            @ApiResponse(responseCode = "204", description = "Nenhuma ação encontrada")
    })
    public ResponseEntity<List<ActionResponseDto>> getActionsByCoordinates(
        @RequestParam("coordinates") String coordinates,
        @RequestParam("radius") Double radius
    ){
        Coordinates coords = new Coordinates(coordinates);

        List<Action> actions = actionService.getByCoordinates(coords.getLatitude(), coords.getLongitude(), radius);

        if(actions.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        List<ActionResponseDto> actionsResponse = ActionMapper.toResponse(actions);
        return ResponseEntity.status(200).body(actionsResponse);
    }

    @GetMapping("/ong/{ongId}")
    @Operation(summary = "Obter todas as ações de uma ONG", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de ações"),
            @ApiResponse(responseCode = "204", description = "Nenhuma ação encontrada")
    })
    public ResponseEntity<List<ActionResponseDto>> getActionsByOng(
        @PathVariable Integer ongId,
        @RequestHeader HashMap<String, String> header
    ){
        List<Action> actions = actionService.getByOng(ongId, header.get("authorization").substring(7));

        if(actions.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        List<ActionResponseDto> actionsResponse = ActionMapper.toResponse(actions);
        return ResponseEntity.status(200).body(actionsResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter uma ação por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Ação encontrada"),
            @ApiResponse(responseCode = "404", description = "Ação não encontrada")
    })
    public ResponseEntity<ActionResponseDto> getActionById(@PathVariable Integer id){
        Action action = actionService.getById(id);

        ActionResponseDto actionResponse = ActionMapper.toResponse(action);
        return ResponseEntity.status(200).body(actionResponse);
    }   

    @PostMapping("/{ongId}")
    @Operation(summary = "Criar uma nova ação", responses = {
            @ApiResponse(responseCode = "201", description = "Ação criada")
    })
    public ResponseEntity<ActionResponseDto> createAction(
        @RequestBody @Valid ActionRequestDto actionDto,
        @PathVariable Integer ongId,
        @RequestHeader HashMap<String, String> header
    ){
        Action action = ActionMapper.toEntity(actionDto);

        Action createdAction = actionService.create(action, ongId, header.get("authorization").substring(7));

        ActionResponseDto actionResponse = ActionMapper.toResponse(createdAction);
        return ResponseEntity.status(201).body(actionResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma ação existente", responses = {
            @ApiResponse(responseCode = "200", description = "Ação atualizada"),
            @ApiResponse(responseCode = "404", description = "Ação não encontrada")
    })
    public ResponseEntity<ActionResponseDto> updateAction(@PathVariable Integer id, @RequestBody ActionRequestDto actionDto){
        Action action = ActionMapper.toEntity(actionDto);

        ActionResponseDto actionResponse = ActionMapper.toResponse(actionService.update(action, id));

        return ResponseEntity.status(200).body(actionResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma ação por ID", responses = {
            @ApiResponse(responseCode = "204", description = "Ação excluída"),
            @ApiResponse(responseCode = "404", description = "Ação não encontrada")
    })
    public ResponseEntity<Void> deleteAction(@PathVariable Integer id){
        actionService.delete(id);
        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{ongId}/{actionId}")
    public ResponseEntity<ActionResponseDto> updateActionStatus(
        @PathVariable("ongId") Integer ongId,
        @PathVariable("actionId") Integer actionId,
        @RequestParam("status") String status,
        @RequestHeader HashMap<String, String> header
    ){
        ActionResponseDto actionResponse = ActionMapper.toResponse(
            actionService.updateStatus(status, actionId, ongId, header.get("authorization").substring(7))
        );

        return ResponseEntity.status(200).body(actionResponse);
    }

    @PatchMapping("/{id}/add-mapping/{mappingId}")
    @Operation(summary = "Adicionar um mapeamento a uma ação", responses = {
            @ApiResponse(responseCode = "200", description = "Mapeamento adicionado"),
            @ApiResponse(responseCode = "404", description = "Ação ou mapeamento não encontrado")
    })
    public ResponseEntity<MappingActionResponseDto> addMapping(
        @PathVariable("id") Integer id,
        @PathVariable("mappingId") Integer mappingId,
        @RequestBody @Valid MappingActionRequestDto mappingActionDto
    ){
        MappingAction mappingAction = MappingActionMapper.toEntity(mappingActionDto);

        MappingActionResponseDto response = MappingActionMapper.toResponse(
            actionService.addMapping(id, mappingId, mappingAction)
        );

        return ResponseEntity.status(200).body(response);
    }
}