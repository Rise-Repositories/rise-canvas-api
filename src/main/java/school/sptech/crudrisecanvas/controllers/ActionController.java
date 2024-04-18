package school.sptech.crudrisecanvas.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import school.sptech.crudrisecanvas.dtos.ActionRequestDto;
import school.sptech.crudrisecanvas.dtos.ActionRequestMapper;
import school.sptech.crudrisecanvas.dtos.ActionResponseDto;
import school.sptech.crudrisecanvas.dtos.ActionResponseMapper;
import school.sptech.crudrisecanvas.entities.Action;
import school.sptech.crudrisecanvas.repositories.ActionRepository;
import school.sptech.crudrisecanvas.repositories.OngRepository;

@RestController
@RequestMapping("/action")
public class ActionController {

    @Autowired
    ActionRepository actionRepository;

    @Autowired
    OngRepository ongRepository;

    @GetMapping
    public ResponseEntity<List<ActionResponseDto>> getActions(){
        List<Action> actions = actionRepository.findAll();
        if(actions.isEmpty()){
            return ResponseEntity.status(404).build();
        }
        List<ActionResponseDto> actionsResponse = ActionResponseMapper.toDto(actions);
        return ResponseEntity.status(200).body(actionsResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionResponseDto> getActionById(Integer id){
        Optional<Action> action = actionRepository.findById(id);
        if(action == null){
            return ResponseEntity.status(404).build();
        }
        ActionResponseDto actionResponse = ActionResponseMapper.toDto(action.get());
        return ResponseEntity.status(200).body(actionResponse);
    }   

    @PostMapping
    public ResponseEntity<ActionResponseDto> createAction(ActionRequestDto action){
        Action newAction = ActionRequestMapper.toEntity(action);
        newAction.getOng().addAction(newAction);

        ongRepository.save(newAction.getOng());

        ActionResponseDto actionResponse = ActionResponseMapper.toDto(actionRepository.save(newAction));
        return ResponseEntity.status(201).body(actionResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionResponseDto> updateAction(Integer id, ActionRequestDto action){
        Optional<Action> actionToUpdate = actionRepository.findById(id);
        if(actionToUpdate.isEmpty()){
            return ResponseEntity.status(404).build();
        }
        Action updatedAction = ActionRequestMapper.toEntity(action);
        updatedAction.setId(id);
        ActionResponseDto actionResponse = ActionResponseMapper.toDto(actionRepository.save(updatedAction));
        return ResponseEntity.status(200).body(actionResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAction(Integer id){
        Optional<Action> actionToDelete = actionRepository.findById(id);
        if(actionToDelete.isEmpty()){
            return ResponseEntity.status(404).build();
        }
        actionRepository.delete(actionToDelete.get());
        return ResponseEntity.status(204).build();
    }
}