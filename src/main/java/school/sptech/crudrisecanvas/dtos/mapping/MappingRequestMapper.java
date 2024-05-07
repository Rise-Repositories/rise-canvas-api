package school.sptech.crudrisecanvas.dtos.mapping;

import school.sptech.crudrisecanvas.entities.Mapping;

public class MappingRequestMapper {
    public static Mapping toEntity(MappingRequestDto mappingRequestDto){
        Mapping mapping = new Mapping();
        mapping.setQtyPeople(mappingRequestDto.getQtyPeople());
        mapping.setDescription(mappingRequestDto.getDescription());
        mapping.setLatitude(mappingRequestDto.getLatitude());
        mapping.setLongitude(mappingRequestDto.getLongitude());
        mapping.setStatus(mappingRequestDto.getStatus());
        return mapping;
    }
    
}
