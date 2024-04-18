package school.sptech.crudrisecanvas.dtos;

import java.util.List;

import school.sptech.crudrisecanvas.entities.Action;

public class ActionResponseMapper {
    public static ActionResponseDto toDto(Action action){
        ActionResponseDto result = new ActionResponseDto();
        result.setId(action.getId());
        result.setName(action.getName());
        result.setDescription(action.getDescription());
        result.setDatetimeStart(action.getDatetimeStart());
        result.setDatetimeEnd(action.getDatetimeEnd());
        result.setLatitude(action.getLatitude());
        result.setLongitude(action.getLongitude());
        result.setOng(OngResponseMapper.toDto(action.getOng()));

        return result;
    }

    public static List<ActionResponseDto> toDto(List<Action> actions){
        return actions.stream().map(ActionResponseMapper::toDto).toList();
    }
    
    public static Action toDto(ActionResponseDto action){
        Action result = new Action();
        result.setId(action.getId());
        result.setName(action.getName());
        result.setDescription(action.getDescription());
        result.setDatetimeStart(action.getDatetimeStart());
        result.setDatetimeEnd(action.getDatetimeEnd());
        result.setLatitude(action.getLatitude());
        result.setLongitude(action.getLongitude());
        return result;
    }
}
