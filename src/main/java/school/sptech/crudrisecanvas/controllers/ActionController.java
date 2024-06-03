package school.sptech.crudrisecanvas.controllers;

import java.util.HashMap;
import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.dtos.action.ActionRequestDto;
import school.sptech.crudrisecanvas.dtos.action.ActionMapper;
import school.sptech.crudrisecanvas.dtos.action.ActionResponseDto;
import school.sptech.crudrisecanvas.dtos.actionVoluntary.ActionVoluntaryDto;
import school.sptech.crudrisecanvas.dtos.actionVoluntary.ActionVoluntaryMapper;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionRequestDto;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionResponseDto;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionMapper;
import school.sptech.crudrisecanvas.entities.Action;
import school.sptech.crudrisecanvas.entities.ActionVoluntary;
import school.sptech.crudrisecanvas.entities.MappingAction;
import school.sptech.crudrisecanvas.service.ActionService;
import school.sptech.crudrisecanvas.service.ActionVoluntaryService;

@RestController
@RequestMapping("/actions")
@RequiredArgsConstructor
public class ActionController {

    private final ActionService actionService;
    private final ActionVoluntaryService actionVoluntaryService;

    @GetMapping
    public ResponseEntity<List<ActionResponseDto>> getActions(){
        List<Action> actions = actionService.getAll();

        if(actions.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        List<ActionResponseDto> actionsResponse = ActionMapper.toResponse(actions);
        return ResponseEntity.status(200).body(actionsResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionResponseDto> getActionById(@PathVariable Integer id){
        Action action = actionService.getById(id);

        ActionResponseDto actionResponse = ActionMapper.toResponse(action);
        return ResponseEntity.status(200).body(actionResponse);
    }   

    @PostMapping("/{ongId}")
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

    @PostMapping("actionVoluntary/{actionId}")
    public ResponseEntity<ActionVoluntaryDto> createActionVoluntary(
            @PathVariable Integer actionId,
        @RequestHeader HashMap<String, String> header
    ){
        ActionVoluntary createActionVoluntary = actionVoluntaryService.createRelation(actionId, header.get("authorization").substring(7));

        ActionVoluntaryDto actionVoluntaryResponse = ActionVoluntaryMapper.toResponse(createActionVoluntary);
        return ResponseEntity.status(201).body(actionVoluntaryResponse);
    }

    @DeleteMapping("actionVoluntary/{relationId}")
    public ResponseEntity<Void> deleteRelation(@PathVariable Integer relationId){
        actionVoluntaryService.deleteRelation(relationId);
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionResponseDto> updateAction(@PathVariable Integer id, @RequestBody ActionRequestDto actionDto){
        Action action = ActionMapper.toEntity(actionDto);

        ActionResponseDto actionResponse = ActionMapper.toResponse(actionService.update(action, id));

        return ResponseEntity.status(200).body(actionResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAction(@PathVariable Integer id){
        actionService.delete(id);
        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{id}/add-mapping/{mappingId}")
    public ResponseEntity<MappingActionResponseDto> addMapping(
        @PathVariable("id") Integer id,
        @PathVariable("mappingId") Integer mappingId,
        @RequestBody @Valid MappingActionRequestDto mappingActionDto
    ){
        MappingAction mappingAction = MappingActionMapper.toEntity(mappingActionDto);

        MappingActionResponseDto response = MappingActionMapper.toResponse(
            actionService.addMapping(id, mappingId, mappingAction)
        );

        //TODO: Enviar email para usuario somente quando finalizar a ação

        // mapping.get().getUsers().stream().forEach(user -> {
        //     emailConfig.sendEmail(
        //         user.getEmail(),
        //         "Rise Canvas - Seu pin foi atendido",
        //         "<h1>Olá, seu pin foi atendido!</h1><br> A ação " + action.get().getName() + " foi realizada e atendeu " + mappingActionBody.getQtyServedPeople() + " pessoas.");
        // });
        return ResponseEntity.status(200).body(response);
    }
}