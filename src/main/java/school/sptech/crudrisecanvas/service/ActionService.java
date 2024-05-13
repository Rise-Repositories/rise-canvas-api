package school.sptech.crudrisecanvas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.entities.Action;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.MappingAction;
import school.sptech.crudrisecanvas.entities.Ong;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.exception.ForbiddenException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.ActionRepository;
import school.sptech.crudrisecanvas.repositories.MappingActionRepository;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

@Service
@RequiredArgsConstructor
public class ActionService {
    private final UserService userService;
    private final MappingService mappingService;
    private final ActionRepository actionRepository;
    private final MappingActionRepository mappingActionRepository;
    
    public List<Action> getAll(){
        List<Action> actions = actionRepository.findAll();
        return actions;
    }
    
    public Action getById(Integer id){
        Optional<Action> action = actionRepository.findById(id);
        
        if(action.isEmpty()){
            throw new NotFoundException("Ação não encontrada");
        }

        return action.get();
    }   

    public Action create(Action action, Integer ongId, String token){
        User user = userService.getAccount(token);

        Ong ong = user.getVoluntary()
                .stream()
                .filter(v -> v.getRole() != VoluntaryRoles.VOLUNTARY && v.getOng().getId() == ongId)
                .findFirst()
                .orElseThrow(() -> new ForbiddenException("Você não tem permissão para criar essa ação"))
                .getOng();

        action.setOng(ong);

        return actionRepository.save(action);
    }

    public Action update(Action action, Integer id){
        Action actionToUpdate = this.getById(id);

        actionToUpdate.setName(action.getName());
        actionToUpdate.setDescription(action.getDescription());
        actionToUpdate.setDatetimeStart(action.getDatetimeStart());
        actionToUpdate.setDatetimeEnd(action.getDatetimeEnd());
        actionToUpdate.setLatitude(action.getLatitude());
        actionToUpdate.setLongitude(action.getLongitude());

        return actionRepository.save(actionToUpdate);
    }

    public void delete(Integer id){
        Action actionToDelete = this.getById(id);
        // List<Integer> ids = actionToDelete.get().getMappigActions().stream().map(e -> e.getId()).toList();
        // mappingActionRepository.deleteAllById(ids);
        actionRepository.delete(actionToDelete);
    }

    public MappingAction addMapping(
        Integer id,
        Integer mappingId,
        MappingAction mappingActionBody
    ){
        // EmailConfig emailConfig = new EmailConfig();

        Action action = this.getById(id);
        Mapping mapping = mappingService.getMappingById(mappingId);

        mappingActionBody.setAction(action);
        mappingActionBody.setMapping(mapping);

        //TODO: Enviar email para usuario somente quando finalizar a ação

        // mapping.get().getUsers().stream().forEach(user -> {
        //     emailConfig.sendEmail(
        //         user.getEmail(),
        //         "Rise Canvas - Seu pin foi atendido",
        //         "<h1>Olá, seu pin foi atendido!</h1><br> A ação " + action.get().getName() + " foi realizada e atendeu " + mappingActionBody.getQtyServedPeople() + " pessoas.");
        // });

        return mappingActionRepository.save(mappingActionBody);
    }
}
