package school.sptech.crudrisecanvas.dtos.mappingAction;

import java.util.List;

import school.sptech.crudrisecanvas.dtos.action.ActionMapper;
import school.sptech.crudrisecanvas.dtos.mapping.MappingMapper;
import school.sptech.crudrisecanvas.entities.MappingAction;

public class MappingActionMapper {

    public static MappingAction toEntity(MappingActionRequestDto dto) {
        MappingAction mappingAction = new MappingAction();
        mappingAction.setQtyServedAdults(dto.getQtyServedAdults());
        mappingAction.setQtyServedChildren(dto.getQtyServedChildren());
        mappingAction.setNoDonation(dto.getNoDonation());
        mappingAction.setNoPeople(dto.getNoPeople());
        mappingAction.setDescription(dto.getDescription());
        return mappingAction;
    }

    public static MappingActionResponseDto toResponse(MappingAction mappingAction) {
        MappingActionResponseDto dto = new MappingActionResponseDto();
        dto.setId(mappingAction.getId());
        dto.setAction(ActionMapper.toNoRelation(mappingAction.getAction()));
        dto.setMapping(MappingMapper.toNoRelationDto(mappingAction.getMapping()));
        dto.setQtyServedAdults(mappingAction.getQtyServedAdults());
        dto.setQtyServedChildren(mappingAction.getQtyServedChildren());
        dto.setNoDonation(mappingAction.getNoDonation());
        dto.setNoPeople(mappingAction.getNoPeople());
        dto.setDescription(mappingAction.getDescription());

        return dto;
    }

    public static MappingActionResponseDto toNoRelationDto(MappingAction mappingAction) {
        MappingActionResponseDto dto = new MappingActionResponseDto();
        dto.setId(mappingAction.getId());
        dto.setAction(ActionMapper.toNoRelation(mappingAction.getAction()));
        dto.setMapping(MappingMapper.toNoRelationDto(mappingAction.getMapping()));
        dto.setQtyServedAdults(mappingAction.getQtyServedAdults());
        dto.setQtyServedChildren(mappingAction.getQtyServedChildren());
        dto.setNoDonation(mappingAction.getNoDonation());
        dto.setNoPeople(mappingAction.getNoPeople());
        dto.setDescription(mappingAction.getDescription());
        return dto;
    }

    public static List<MappingActionResponseDto> toNoRelationDto(List<MappingAction> mappingAction) {
        return mappingAction == null
                ? null
                : mappingAction.stream().map(MappingActionMapper::toNoRelationDto).toList();
    }
    
}
