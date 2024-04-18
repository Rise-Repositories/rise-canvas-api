package school.sptech.crudrisecanvas.dtos;


import java.util.List;

import school.sptech.crudrisecanvas.entities.Mapping;

public class MappingResponseMapper {
    public static MappingResponseDto toDto(Mapping mapping){
        MappingResponseDto mappingResponse = new MappingResponseDto();
        mappingResponse.setId(mapping.getId());
        mappingResponse.setDescription(mapping.getDescription());
        mappingResponse.setLatitute(mapping.getLatitute());
        mappingResponse.setLongitude(mapping.getLongitude());
        mappingResponse.setStatus(mapping.getStatus().toString());
        mappingResponse.setDate(mapping.getDate().toString());
        mappingResponse.setUser(UserResponseMapper.toNoRelationDto(mapping.getUser()));
        return mappingResponse;
    }

    public static List<MappingResponseDto> toDto(List<Mapping> mappings){
        return mappings.stream().map(MappingResponseMapper::toDto).toList();
    }
    
    public static MappingResponseNoRelationDto toNoRelationDto(Mapping mapping){
        MappingResponseNoRelationDto mappingResponse = new MappingResponseNoRelationDto();
        mappingResponse.setId(mapping.getId());
        mappingResponse.setDescription(mapping.getDescription());
        mappingResponse.setLatitute(mapping.getLatitute());
        mappingResponse.setLongitude(mapping.getLongitude());
        mappingResponse.setStatus(mapping.getStatus().toString());
        mappingResponse.setDate(mapping.getDate().toString());
        return mappingResponse;
    }

    public static List<MappingResponseNoRelationDto> toNoRelationDto(List<Mapping> mappings){
        return mappings.stream().map(MappingResponseMapper::toNoRelationDto).toList();
    }
}
