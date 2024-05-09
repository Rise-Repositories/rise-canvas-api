package school.sptech.crudrisecanvas.dtos.mappingAction;

import school.sptech.crudrisecanvas.dtos.action.ActionMapper;
import school.sptech.crudrisecanvas.dtos.mapping.MappingMapper;
import school.sptech.crudrisecanvas.entities.MappingAction;

public class MappingActionMapper {

    public static MappingAction toEntity(MappingActionRequestDto dto) {
        MappingAction mappingAction = new MappingAction();
        mappingAction.setQtyServedPeople(dto.getQtyServedPeople());
        return mappingAction;
    }

    public static MappingActionResponseDto toDto(MappingAction mappingAction) {
        MappingActionResponseDto dto = new MappingActionResponseDto();
        dto.setId(mappingAction.getId());
        dto.setAction(ActionMapper.toNoRelation(mappingAction.getAction()));
        dto.setMapping(MappingMapper.toNoRelationDto(mappingAction.getMapping()));
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
