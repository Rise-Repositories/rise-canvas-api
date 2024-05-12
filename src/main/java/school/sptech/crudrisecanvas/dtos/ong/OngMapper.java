package school.sptech.crudrisecanvas.dtos.ong;

import java.util.List;

import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryMapper;
import school.sptech.crudrisecanvas.dtos.action.ActionMapper;
import school.sptech.crudrisecanvas.entities.Ong;

public class OngMapper {
    public static Ong toEntity(OngRequestDto ongRequestDto) {
        Ong ong = new Ong();
        ong.setName(ongRequestDto.getName());
        ong.setCnpj(ongRequestDto.getCnpj());
        ong.setDescription(ongRequestDto.getDescription());
        ong.setCep(ongRequestDto.getCep());
        ong.setAddress(ongRequestDto.getAddress());
        return ong;
    }

    public static Ong toEntity(OngRequestUpdateDto ongUpdateDto) {
        Ong ong = new Ong();
        ong.setName(ongUpdateDto.getName());
        ong.setCnpj(ongUpdateDto.getCnpj());
        ong.setDescription(ongUpdateDto.getDescription());
        ong.setCep(ongUpdateDto.getCep());
        ong.setAddress(ongUpdateDto.getAddress());
        return ong;
    }
    
    public static OngResponseDto toResponse(Ong ong) {
        OngResponseDto dto = new OngResponseDto();
        dto.setId(ong.getId());
        dto.setName(ong.getName());
        dto.setCnpj(ong.getCnpj());
        dto.setCep(ong.getCep());
        dto.setDescription(ong.getDescription());
        dto.setAddress(ong.getAddress());
        dto.setStatus(ong.getStatus());
        dto.setActions(ActionMapper.toNoRelation(ong.getActions()));
        dto.setVoluntaries(VoluntaryMapper.toNoRelationDto(ong.getVoluntaries()));
        return dto;
    }

    public static List<OngResponseDto> toResponse(List<Ong> ongs) {
        return ongs == null
            ? null 
            : ongs.stream().map(OngMapper::toResponse).toList();
    }

    public static OngResponseNoRelationDto toNoRelationDto(Ong ong) {
        if(ong == null) return null;
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
            : ongs.stream().map(OngMapper::toNoRelationDto).toList();
    }
}
