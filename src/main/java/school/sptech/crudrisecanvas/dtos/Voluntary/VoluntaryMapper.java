package school.sptech.crudrisecanvas.dtos.Voluntary;

import java.util.List;

import school.sptech.crudrisecanvas.dtos.user.UserMapper;
import school.sptech.crudrisecanvas.entities.Voluntary;

public class VoluntaryMapper {
    public static VoluntaryResponseNoRelationDto toNoRelationDto(Voluntary voluntary) {
        if(voluntary == null) return null;
        VoluntaryResponseNoRelationDto dto = new VoluntaryResponseNoRelationDto();
        dto.setId(voluntary.getId());
        dto.setRole(voluntary.getRole());
        dto.setUser(UserMapper.toNoRelationDto(voluntary.getUser()));
        return dto;
    }

    public static List<VoluntaryResponseNoRelationDto> toNoRelationDto(List<Voluntary> voluntaries) {
        return voluntaries == null
            ? null 
            : voluntaries.stream().map(VoluntaryMapper::toNoRelationDto).toList();
    }
    
}
