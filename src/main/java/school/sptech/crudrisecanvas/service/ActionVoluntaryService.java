package school.sptech.crudrisecanvas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.sptech.crudrisecanvas.entities.Action;
import school.sptech.crudrisecanvas.entities.ActionVoluntary;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.entities.Voluntary;
import school.sptech.crudrisecanvas.exception.ForbiddenException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.ActionVoluntaryRepository;

@Service
@RequiredArgsConstructor
public class ActionVoluntaryService {
    private final ActionVoluntaryRepository repository;
    private final UserService userService;
    private final ActionService actionService;

    public ActionVoluntary createRelation(Integer actionId, String token){
        User user = userService.getAccount(token);
        Action action = actionService.getById(actionId);

        ActionVoluntary actionVoluntary = new ActionVoluntary();

        Voluntary voluntary = user.getVoluntary()
                .stream()
                .filter(v -> v.getOng().getId() == action.getOng().getId())
                .findFirst()
                .orElseThrow(() -> new ForbiddenException("Você não tem permissão para criar essa ação"));

        actionVoluntary.setVoluntary(voluntary);
        actionVoluntary.setAction(actionService.getById(actionId));

        return repository.save(actionVoluntary);
    }

    public void deleteRelation(int id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }else{
            throw new NotFoundException("ID não encontrado");
        }
    }

}
