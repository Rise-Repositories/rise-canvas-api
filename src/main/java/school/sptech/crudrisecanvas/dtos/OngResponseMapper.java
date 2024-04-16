package school.sptech.crudrisecanvas.dtos;

import java.util.List;

import lombok.Data;
import school.sptech.crudrisecanvas.entities.Ong;

@Data
public class OngResponseMapper {
    public static OngResponseDto toDto(Ong ong) {
        OngResponseDto dto = new OngResponseDto();
        dto.setId(ong.getId());
        dto.setName(ong.getName());
        dto.setEmail(ong.getEmail());
        dto.setCnpj(ong.getCnpj());
        dto.setAddress(ong.getAddress());
        dto.setStatus(ong.getStatus());
        return dto;
    }

    public static Ong toEntity(OngResponseDto dto) {
        Ong ong = new Ong();
        ong.setId(dto.getId());
        ong.setName(dto.getName());
        ong.setEmail(dto.getEmail());
        ong.setCnpj(dto.getCnpj());
        ong.setAddress(dto.getAddress());
        ong.setStatus(dto.getStatus());
        return ong;
    }

    public static List<OngResponseDto> toDto(List<Ong> ongs) {
        return ongs.stream().map(OngResponseMapper::toDto).toList();
    }
}
