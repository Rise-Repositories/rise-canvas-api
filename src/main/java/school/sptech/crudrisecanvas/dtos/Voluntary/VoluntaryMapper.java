package school.sptech.crudrisecanvas.dtos.Voluntary;

import java.util.List;

import school.sptech.crudrisecanvas.dtos.ong.OngMapper;
import school.sptech.crudrisecanvas.dtos.user.UserMapper;
import school.sptech.crudrisecanvas.entities.Voluntary;

public class VoluntaryMapper {

    public static Voluntary toEntity(VoluntaryRequestDto dto) {
        if(dto == null) return null;
        Voluntary voluntary = new Voluntary();
        voluntary.setRole(dto.getRole());
        voluntary.setUser(UserMapper.toEntity(dto.getUser()));
        return voluntary;
    }

    public static VoluntaryOngResponseNoRelationDto toOngNoRelationDto(Voluntary voluntary) {
        if(voluntary == null) return null;
        VoluntaryOngResponseNoRelationDto dto = new VoluntaryOngResponseNoRelationDto();
        dto.setId(voluntary.getId());
        dto.setRole(voluntary.getRole());
        dto.setUser(UserMapper.toNoRelationDto(voluntary.getUser()));
        return dto;
    }

    public static List<VoluntaryOngResponseNoRelationDto> toOngNoRelationDto(List<Voluntary> voluntaries) {
        return voluntaries == null
            ? null 
            : voluntaries.stream().map(VoluntaryMapper::toOngNoRelationDto).toList();
    }

    public static VoluntaryUserResponseNoRelationDto toUserNoRelationDto(Voluntary voluntary) {
        if(voluntary == null) return null;
        VoluntaryUserResponseNoRelationDto dto = new VoluntaryUserResponseNoRelationDto();
        dto.setId(voluntary.getId());
        dto.setRole(voluntary.getRole());
        dto.setOng(OngMapper.toNoRelationDto(voluntary.getOng()));
        return dto;
    }

    public static List<VoluntaryUserResponseNoRelationDto> toUserNoRelationDto(List<Voluntary> voluntaries) {
        return voluntaries == null
            ? null 
            : voluntaries.stream().map(VoluntaryMapper::toUserNoRelationDto).toList();
    }
    
}
