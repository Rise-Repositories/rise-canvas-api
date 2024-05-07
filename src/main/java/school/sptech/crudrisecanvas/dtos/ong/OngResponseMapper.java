package school.sptech.crudrisecanvas.dtos.ong;

import java.util.List;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.action.ActionResponseMapper;
import school.sptech.crudrisecanvas.entities.Ong;

@Data
public class OngResponseMapper {
    public static OngResponseDto toDto(Ong ong) {
        OngResponseDto dto = new OngResponseDto();
        dto.setId(ong.getId());
        dto.setName(ong.getName());
        dto.setCnpj(ong.getCnpj());
        dto.setCep(ong.getCep());
        dto.setDescription(ong.getDescription());
        dto.setAddress(ong.getAddress());
        dto.setStatus(ong.getStatus());
        dto.setActions(ActionResponseMapper.toNoRelationDto(ong.getActions()));
        return dto;
    }

    public static List<OngResponseDto> toDto(List<Ong> ongs) {
        return ongs.stream().map(OngResponseMapper::toDto).toList();
    }

    public static OngResponseNoRelationDto toNoRelationDto(Ong ong) {
        OngResponseNoRelationDto dto = new OngResponseNoRelationDto();
        dto.setId(ong.getId());
        dto.setName(ong.getName());
        dto.setCnpj(ong.getCnpj());
        dto.setCep(ong.getCep());
        dto.setAddress(ong.getAddress());
        dto.setStatus(ong.getStatus());
        return dto;
    }

    public static List<OngResponseNoRelationDto> toNoRelationDto(List<Ong> ongs) {
        return ongs == null 
            ? null
            : ongs.stream().map(OngResponseMapper::toNoRelationDto).toList();
    }
}
