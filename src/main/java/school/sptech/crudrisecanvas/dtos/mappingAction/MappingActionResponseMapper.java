package school.sptech.crudrisecanvas.dtos.mappingAction;

import school.sptech.crudrisecanvas.dtos.action.ActionResponseMapper;
import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseMapper;
import school.sptech.crudrisecanvas.entities.MappingAction;

public class MappingActionResponseMapper {
    public static MappingActionResponseDto toDto(MappingAction mappingAction) {
        MappingActionResponseDto dto = new MappingActionResponseDto();
        dto.setId(mappingAction.getId());
        dto.setAction(ActionResponseMapper.toNoRelationDto(mappingAction.getAction()));
        dto.setMapping(MappingResponseMapper.toNoRelationDto(mappingAction.getMapping()));
        dto.setQtyServedPeople(mappingAction.getQtyServedPeople());
        return dto;
    }

    public static MappingActionResponseDto toNoRelationDto(MappingAction mappingAction) {
        MappingActionResponseDto dto = new MappingActionResponseDto();
        dto.setId(mappingAction.getId());
        dto.setQtyServedPeople(mappingAction.getQtyServedPeople());
        return dto;
    }
    
}
