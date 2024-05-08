package school.sptech.crudrisecanvas.controllers;

import java.util.List;
import java.util.Optional;

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
import school.sptech.crudrisecanvas.dtos.action.ActionRequestDto;
import school.sptech.crudrisecanvas.dtos.action.ActionRequestMapper;
import school.sptech.crudrisecanvas.dtos.action.ActionResponseDto;
import school.sptech.crudrisecanvas.dtos.action.ActionResponseMapper;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionRequestDto;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionResponseDto;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionResponseMapper;
import school.sptech.crudrisecanvas.entities.Action;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.MappingAction;
import school.sptech.crudrisecanvas.entities.Ong;
import school.sptech.crudrisecanvas.repositories.ActionRepository;
import school.sptech.crudrisecanvas.repositories.MappingActionRepository;
import school.sptech.crudrisecanvas.repositories.MappingRepository;
import school.sptech.crudrisecanvas.repositories.OngRepository;
import school.sptech.crudrisecanvas.utils.EmailConfig;

@RestController
@RequestMapping("/actions")
public class ActionController {

    @Autowired
    ActionRepository actionRepository;

    @Autowired
    OngRepository ongRepository;

    @Autowired
    MappingRepository mappingRepository;

    @Autowired
    MappingActionRepository mappingActionRepository;

    @GetMapping
    public ResponseEntity<List<ActionResponseDto>> getActions(){
        List<Action> actions = actionRepository.findAll();
        if(actions.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        List<ActionResponseDto> actionsResponse = ActionResponseMapper.toDto(actions);
        return ResponseEntity.status(200).body(actionsResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionResponseDto> getActionById(Integer id){
        Optional<Action> action = actionRepository.findById(id);
        if(action.isEmpty()){
            return ResponseEntity.status(404).build();
        }
        ActionResponseDto actionResponse = ActionResponseMapper.toDto(action.get());
        return ResponseEntity.status(200).body(actionResponse);
    }   

    @PostMapping("/{id}")
    public ResponseEntity<ActionResponseDto> createAction(@PathVariable Integer id,@RequestBody @Valid ActionRequestDto action){
        Action newAction = ActionRequestMapper.toEntity(action);

        Optional<Ong> ong = ongRepository.findById(id);

        if(ong.isEmpty()){
            return ResponseEntity.status(404).build();
        }
        newAction.setOng(ong.get());

        ActionResponseDto actionResponse = ActionResponseMapper.toDto(actionRepository.save(newAction));
        return ResponseEntity.status(201).body(actionResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionResponseDto> updateAction(@PathVariable Integer id, @RequestBody ActionRequestDto action){
        Optional<Action> actionToUpdate = actionRepository.findById(id);
        if(actionToUpdate.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        Action updatedAction = actionToUpdate.get();

        updatedAction.setName(action.getName());
        updatedAction.setDescription(action.getDescription());
        updatedAction.setDatetimeStart(action.getDatetimeStart());
        updatedAction.setDatetimeEnd(action.getDatetimeEnd());
        updatedAction.setLatitude(action.getLatitude());
        updatedAction.setLongitude(action.getLongitude());

        ActionResponseDto actionResponse = ActionResponseMapper.toDto(actionRepository.save(updatedAction));
        return ResponseEntity.status(200).body(actionResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAction(@PathVariable Integer id){
        Optional<Action> actionToDelete = actionRepository.findById(id);
        if(actionToDelete.isEmpty()){
            return ResponseEntity.status(404).build();
        }
        List<Integer> ids = actionToDelete.get().getMappingActions().stream().map(e -> e.getId()).toList();
        mappingActionRepository.deleteAllById(ids);
        actionRepository.deleteById(id);
        return ResponseEntity.status(204).build();
    }

    @PostMapping("/{id}/add-mapping/{mappingId}")
    public ResponseEntity<MappingActionResponseDto> addMapping(
        @PathVariable("id") Integer id,
        @PathVariable("mappingId") Integer mappingId,
        @RequestBody @Valid MappingActionRequestDto mappingActionBody
    ){
        EmailConfig emailConfig = new EmailConfig();
        Optional<Action> action = actionRepository.findById(id);
        Optional<Mapping> mapping = mappingRepository.findById(mappingId);
        if(action.isEmpty() || mapping.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        MappingAction mappingAction = new MappingAction(action.get(), mapping.get(), mappingActionBody.getQtyServedPeople());

        MappingActionResponseDto response = MappingActionResponseMapper.toDto(mappingActionRepository.save(mappingAction));

        mapping.get().getUsers().stream().forEach(user -> {
            emailConfig.sendEmail(
                user.getEmail(),
                "Rise Canvas - Seu pin foi atendido",
                "<h1>Olá, seu pin foi atendido!</h1><br> A ação " + action.get().getName() + " foi realizada e atendeu " + mappingActionBody.getQtyServedPeople() + " pessoas.");
        });
        return ResponseEntity.status(200).body(response);
    }
}