package school.sptech.crudrisecanvas.dtos.action;

import java.util.List;

import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionMapper;
import school.sptech.crudrisecanvas.dtos.ong.OngMapper;
import school.sptech.crudrisecanvas.dtos.tags.TagsMapper;
import school.sptech.crudrisecanvas.entities.Action;

public class ActionMapper {
    public static Action toEntity(ActionRequestDto actionRequestDto){
        Action action = new Action();
        action.setName(actionRequestDto.getName());
        action.setDescription(actionRequestDto.getDescription());
        action.setDatetimeStart(actionRequestDto.getDateTimeStart());
        action.setDatetimeEnd(actionRequestDto.getDateTimeEnd());
        action.setLatitude(actionRequestDto.getLatitude());
        action.setLongitude(actionRequestDto.getLongitude());
        action.setRadius(actionRequestDto.getRadius());
        action.setStatus(actionRequestDto.getStatus());
        action.setTags(TagsMapper.toEntity(actionRequestDto.getTags()));
        return action;
    }
    
    public static ActionResponseDto toResponse(Action action){
        ActionResponseDto result = new ActionResponseDto();
        result.setId(action.getId());
        result.setName(action.getName());
        result.setDescription(action.getDescription());
        result.setDatetimeStart(action.getDatetimeStart());
        result.setDatetimeEnd(action.getDatetimeEnd());
        result.setLatitude(action.getLatitude());
        result.setLongitude(action.getLongitude());
        result.setRadius(action.getRadius());
        result.setMappingAction(MappingActionMapper.toNoRelationDto(action.getMappingActions()));
        result.setOng(OngMapper.toNoRelationDto(action.getOng()));
        result.setStatus(action.getStatus());
        result.setTags(TagsMapper.toResponse(action.getTags()));

        return result;
    }

    public static List<ActionResponseDto> toResponse(List<Action> actions){
        return actions == null
            ? null
            : actions.stream().map(ActionMapper::toResponse).toList();
    }

    public static ActionResponseNoRelationDto toNoRelation(Action action){
        ActionResponseNoRelationDto result = new ActionResponseNoRelationDto();
        result.setId(action.getId());
        result.setName(action.getName());
        result.setDescription(action.getDescription());
        result.setDatetimeStart(action.getDatetimeStart());
        result.setDatetimeEnd(action.getDatetimeEnd());
        result.setLatitude(action.getLatitude());
        result.setLongitude(action.getLongitude());
        result.setRadius(action.getRadius());
        result.setStatus(action.getStatus());
        result.setTags(TagsMapper.toResponse(action.getTags()));

        return result;
    }

    public static List<ActionResponseNoRelationDto> toNoRelation(List<Action> actions){
        return actions == null 
            ? null 
            : actions.stream().map(ActionMapper::toNoRelation).toList();
    }

    public static ActionResponseNoMappingRelationDto toNoMappingRelation(Action action){
        ActionResponseNoMappingRelationDto result = new ActionResponseNoMappingRelationDto();
        result.setId(action.getId());
        result.setName(action.getName());
        result.setDescription(action.getDescription());
        result.setDatetimeStart(action.getDatetimeStart());
        result.setDatetimeEnd(action.getDatetimeEnd());
        result.setLatitude(action.getLatitude());
        result.setLongitude(action.getLongitude());
        result.setRadius(action.getRadius());
        result.setOng(OngMapper.toNoRelationDto(action.getOng()));
        result.setStatus(action.getStatus());
        result.setTags(TagsMapper.toResponse(action.getTags()));

        return result;
    }

    public static List<ActionResponseNoMappingRelationDto> toNoMappingRelation(List<Action> actions){
        return actions == null
                ? null
                : actions.stream().map(ActionMapper::toNoMappingRelation).toList();
    }
}
