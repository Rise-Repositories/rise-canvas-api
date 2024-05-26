package school.sptech.crudrisecanvas.dtos.actionVoluntary;

import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryMapper;
import school.sptech.crudrisecanvas.dtos.action.ActionMapper;
import school.sptech.crudrisecanvas.entities.Action;
import school.sptech.crudrisecanvas.entities.ActionVoluntary;
import school.sptech.crudrisecanvas.entities.Voluntary;

public class ActionVoluntaryMapper {

    public static ActionVoluntary toEntity(Action action, Voluntary voluntary) {
        if (action == null || voluntary == null) return null;

        ActionVoluntary actionVoluntary = new ActionVoluntary();
        actionVoluntary.setAction(action);
        actionVoluntary.setVoluntary(voluntary);

        return actionVoluntary;
    }

    public static ActionVoluntaryDto toResponse(ActionVoluntary actionVoluntary) {
        if (actionVoluntary == null) return null;

        ActionVoluntaryDto result = new ActionVoluntaryDto();
        result.setId(actionVoluntary.getId());
        result.setAction(ActionMapper.toResponse( actionVoluntary.getAction()));
        result.setVoluntary(VoluntaryMapper.toResponse(actionVoluntary.getVoluntary()));

        return result;
    }
}
