package school.sptech.crudrisecanvas.dtos.mapping;

import java.util.List;

import school.sptech.crudrisecanvas.dtos.user.UserMapper;
import school.sptech.crudrisecanvas.entities.Mapping;

public class MappingMapper {
    public static Mapping toEntity(MappingRequestDto mappingRequestDto){
        Mapping mapping = new Mapping();
        mapping.setQtyPeople(mappingRequestDto.getQtyPeople());
        mapping.setDescription(mappingRequestDto.getDescription());
        mapping.setLatitude(mappingRequestDto.getLatitude());
        mapping.setLongitude(mappingRequestDto.getLongitude());
        mapping.setStatus(mappingRequestDto.getStatus());
        return mapping;
    }

    public static MappingResponseDto toDto(Mapping mapping){
        MappingResponseDto mappingResponse = new MappingResponseDto();
        mappingResponse.setId(mapping.getId());
        mappingResponse.setQtyPeople(mapping.getQtyPeople());
        mappingResponse.setDescription(mapping.getDescription());
        mappingResponse.setLatitude(mapping.getLatitude());
        mappingResponse.setLongitude(mapping.getLongitude());
        mappingResponse.setStatus(mapping.getStatus().toString());
        mappingResponse.setDate(mapping.getDate().toString());
        mappingResponse.setUsers(UserMapper.toNoRelationDto(mapping.getUsers()));
        return mappingResponse;
    }

    public static List<MappingResponseDto> toDto(List<Mapping> mappings){
        return mappings == null
            ? null
            : mappings.stream().map(MappingMapper::toDto).toList();
    }
    
    public static MappingResponseNoRelationDto toNoRelationDto(Mapping mapping){
        MappingResponseNoRelationDto mappingResponse = new MappingResponseNoRelationDto();
        mappingResponse.setId(mapping.getId());
        mappingResponse.setQtyPeople(mapping.getQtyPeople());
        mappingResponse.setDescription(mapping.getDescription());
        mappingResponse.setLatitude(mapping.getLatitude());
        mappingResponse.setLongitude(mapping.getLongitude());
        mappingResponse.setStatus(mapping.getStatus().toString());
        mappingResponse.setDate(mapping.getDate().toString());
        return mappingResponse;
    }

    public static List<MappingResponseNoRelationDto> toNoRelationDto(List<Mapping> mappings){
        return mappings == null 
            ? null 
            : mappings.stream().map(MappingMapper::toNoRelationDto).toList();
    }
    
}