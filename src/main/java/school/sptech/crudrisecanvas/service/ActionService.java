package school.sptech.crudrisecanvas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.entities.Action;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.MappingAction;
import school.sptech.crudrisecanvas.entities.Ong;
import school.sptech.crudrisecanvas.entities.Tags;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.exception.ForbiddenException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.ActionRepository;
import school.sptech.crudrisecanvas.repositories.MappingActionRepository;
import school.sptech.crudrisecanvas.utils.Coordinates;
import school.sptech.crudrisecanvas.utils.Enums.ActionStatus;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;
import school.sptech.crudrisecanvas.utils.adpters.MailValue;

@Service
@RequiredArgsConstructor
public class ActionService {
    private final UserService userService;
    private final MappingService mappingService;
    private final ActionRepository actionRepository;
    private final MappingActionRepository mappingActionRepository;
    private final TagsService tagsService;
    
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

    public List<Action> getByCoordinates(Double latitude, Double longitude, Double radius){
        List<Action> actions = actionRepository.findByCordinates(latitude, longitude, radius);
        return actions;
    }

    public Action create(Action action, Integer ongId, List<Integer> ids, String token){
        User user = userService.getAccount(token);

        Ong ong = user.getVoluntary()
                .stream()
                .filter(v -> v.getRole() != VoluntaryRoles.VOLUNTARY && v.getOng().getId() == ongId)
                .findFirst()
                .orElseThrow(() -> new ForbiddenException("Você não tem permissão para criar essa ação"))
                .getOng();

        List<Tags> tags = tagsService.getManyByIds(ids);

        action.setStatus(ActionStatus.PENDING.toString());
        action.setOng(ong);
        action.setTags(tags);

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
        Action action = this.getById(id);
        Mapping mapping = mappingService.getMappingById(mappingId);

        mappingActionBody.setAction(action);
        mappingActionBody.setMapping(mapping);

        mapping.getUsersMappings().stream().forEach(userMapping -> {
            ScheduleService.add(
                new MailValue(
                    userMapping.getUser().getEmail(),
                    "Rise Canvas - Seu pin foi atendido",
                    "<h1>Olá, seu pin foi atendido!</h1><br> A ação " + action.getName() + " foi realizada e atendeu " + (mappingActionBody.getQtyServedAdults() + mappingActionBody.getQtyServedChildren()) + " pessoas."
                )
            );
        });

        return mappingActionRepository.save(mappingActionBody);
    }

    public Action updateStatus(String requestStatus, Integer actionId, Integer ongId, String token){
        User user = userService.getAccount(token);

        user.getVoluntary()
                .stream()
                .filter(v -> v.getRole() != VoluntaryRoles.VOLUNTARY && v.getOng().getId() == ongId)
                .findFirst()
                .orElseThrow(() -> new ForbiddenException("Você não tem permissão para alterar o status dessa ação"));

        Action action = this.getById(actionId);
        try{
            ActionStatus status = ActionStatus.valueOf(requestStatus);

            action.setStatus(status.toString());
        }
        catch(IllegalArgumentException e){
            throw new NotFoundException("Status não encontrado");
        }
        
        return actionRepository.save(action);
    }

    public List<Action> getByOng(Integer ongId, String token){
        User user = userService.getAccount(token);

        user.getVoluntary()
                .stream()
                .filter(v -> v.getRole() != VoluntaryRoles.VOLUNTARY && v.getOng().getId() == ongId)
                .findFirst()
                .orElseThrow(() -> new ForbiddenException("Você não tem permissão para criar essa ação"));

        return actionRepository.findAllByOngId(ongId);
    }

    public List<Mapping> getActionMappingsByCoordinates(Integer actionId){
        Action action = getById(actionId);
        Coordinates coordinates = new Coordinates(action.getLatitude(), action.getLongitude());
        if (action.getStatus().equals("PENDING") || action.getStatus().equals("IN_PROGRESS")) {

            return mappingService.getMappingsByCoordinates(
                    coordinates,
                    action.getRadius()
            );
        } else {
            return mappingService.getDonatedMappingsByCoordinates(
                    coordinates,
                    action.getRadius(),
                    action.getId()
            );
        }
    }
}
