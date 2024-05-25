package school.sptech.crudrisecanvas.dtos.Voluntary;

import java.util.List;

import school.sptech.crudrisecanvas.dtos.ong.OngMapper;
import school.sptech.crudrisecanvas.dtos.user.UserMapper;
import school.sptech.crudrisecanvas.entities.Voluntary;

public class VoluntaryMapper {
    public static VoluntaryOngResponseDto toOngNoRelationDto(Voluntary voluntary) {
        if(voluntary == null) return null;
        VoluntaryOngResponseDto dto = new VoluntaryOngResponseDto();
        dto.setId(voluntary.getId());
        dto.setRole(voluntary.getRole());
        dto.setUser(UserMapper.toNoRelationDto(voluntary.getUser()));
        return dto;
    }

    public static List<VoluntaryOngResponseDto> toOngNoRelationDto(List<Voluntary> voluntaries) {
        return voluntaries == null
            ? null 
            : voluntaries.stream().map(VoluntaryMapper::toOngNoRelationDto).toList();
    }

    public static VoluntaryUserResponseDto toUserNoRelationDto(Voluntary voluntary) {
        if(voluntary == null) return null;
        VoluntaryUserResponseDto dto = new VoluntaryUserResponseDto();
        dto.setId(voluntary.getId());
        dto.setRole(voluntary.getRole());
        dto.setOng(OngMapper.toNoRelationDto(voluntary.getOng()));
        return dto;
    }

    public static VoluntaryResponseDto toResponse(Voluntary voluntary){
        if(voluntary == null) return null;
        VoluntaryResponseDto dto = new VoluntaryResponseDto();
        dto.setId(voluntary.getId());
        dto.setRole(voluntary.getRole());
        dto.setOng(OngMapper.toNoRelationDto(voluntary.getOng()));
        dto.setUser(UserMapper.toNoRelationDto(voluntary.getUser()));
        return dto;
    }


    public static List<VoluntaryUserResponseDto> toUserNoRelationDto(List<Voluntary> voluntaries) {
        return voluntaries == null
            ? null 
            : voluntaries.stream().map(VoluntaryMapper::toUserNoRelationDto).toList();
    }
    
}
