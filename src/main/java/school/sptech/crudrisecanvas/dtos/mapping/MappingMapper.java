package school.sptech.crudrisecanvas.dtos.mapping;

import java.util.List;

import school.sptech.crudrisecanvas.dtos.address.AddressMapper;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionMapper;
import school.sptech.crudrisecanvas.dtos.userMapping.UserMappingMapper;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.UserMapping;

public class MappingMapper {
    public static Mapping toEntity(MappingRequestDto mappingRequestDto){
        Mapping mapping = new Mapping();
        mapping.setQtyAdults(mappingRequestDto.getQtyAdults());
        mapping.setQtyChildren(mappingRequestDto.getQtyChildren());
        mapping.setReferencePoint(mappingRequestDto.getReferencePoint());
        mapping.setHasDisorders(mappingRequestDto.getHasDisorders());
        mapping.setDescription(mappingRequestDto.getDescription());
        mapping.setLatitude(mappingRequestDto.getLatitude());
        mapping.setLongitude(mappingRequestDto.getLongitude());
        mapping.setAddress(AddressMapper.toEntity(mappingRequestDto.getAddress()));
        return mapping;
    }

    public static MappingResponseDto toResponse(Mapping mapping){
        MappingResponseDto mappingResponse = new MappingResponseDto();
        mappingResponse.setId(mapping.getId());
        mappingResponse.setQtyAdults(mapping.getQtyAdults());
        mappingResponse.setQtyChildren(mapping.getQtyChildren());
        mappingResponse.setHasDisorders(mapping.getHasDisorders());
        mappingResponse.setReferencePoint(mapping.getReferencePoint());
        mappingResponse.setDescription(mapping.getDescription());
        mappingResponse.setLatitude(mapping.getLatitude());
        mappingResponse.setLongitude(mapping.getLongitude());
        mappingResponse.setStatus(mapping.getStatus().toString());
        mappingResponse.setDate(mapping.getDate().toString());
        mappingResponse.setUserMappings(UserMappingMapper.toMapping(mapping.getUsersMappings()));
        mappingResponse.setAddress(AddressMapper.toDto(mapping.getAddress()));
        return mappingResponse;
    }

    public static List<MappingResponseDto> toResponse(List<Mapping> mappings){
        return mappings == null
            ? null
            : mappings.stream().map(MappingMapper::toResponse).toList();
    }
    
    public static MappingResponseNoRelationDto toNoRelationDto(Mapping mapping){
        MappingResponseNoRelationDto mappingResponse = new MappingResponseNoRelationDto();
        mappingResponse.setId(mapping.getId());
        mappingResponse.setQtyAdults(mapping.getQtyAdults());
        mappingResponse.setQtyChildren(mapping.getQtyChildren());
        mappingResponse.setHasDisorders(mapping.getHasDisorders());
        mappingResponse.setReferencePoint(mapping.getReferencePoint());
        mappingResponse.setDescription(mapping.getDescription());
        mappingResponse.setLatitude(mapping.getLatitude());
        mappingResponse.setLongitude(mapping.getLongitude());
        mappingResponse.setStatus(mapping.getStatus().toString());
        mappingResponse.setDate(mapping.getDate().toString());
        mappingResponse.setMappingActions(MappingActionMapper.toNoMappingRelationDto(mapping.getMappingActions()));
        return mappingResponse;
    }

    public static List<MappingResponseNoRelationDto> toNoRelationDto(List<Mapping> mappings){
        return mappings == null
                ? null
                : mappings.stream().map(MappingMapper::toNoRelationDto).toList();
    }

    public static MappingResponseNoRelationDto toUserNoRelationDto(UserMapping userMapping){
        Mapping mapping = userMapping.getMapping();
        return toNoRelationDto(mapping);
    }

    public static List<MappingResponseNoRelationDto> toUserNoRelationDto(List<UserMapping> userMappings){
        return userMappings == null
                ? null
                : userMappings.stream().map(MappingMapper::toUserNoRelationDto).toList();
    }

}
