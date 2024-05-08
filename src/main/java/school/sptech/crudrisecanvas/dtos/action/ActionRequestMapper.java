package school.sptech.crudrisecanvas.dtos.action;

import school.sptech.crudrisecanvas.entities.Action;

public class ActionRequestMapper {
    public static Action toEntity(ActionRequestDto actionRequestDto){
        Action action = new Action();
        action.setName(actionRequestDto.getName());
        action.setDescription(actionRequestDto.getDescription());
        action.setDatetimeStart(actionRequestDto.getDatetimeStart());
        action.setDatetimeEnd(actionRequestDto.getDatetimeEnd());
        action.setLatitude(actionRequestDto.getLatitude());
        action.setLongitude(actionRequestDto.getLongitude());
        return action;
    }
    
}
