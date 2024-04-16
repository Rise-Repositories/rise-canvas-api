package school.sptech.crudrisecanvas.dtos;

import school.sptech.crudrisecanvas.entities.Ong;

public class OngRequestMapper {
    public  static OngRequestDto toDto(Ong ong) {
        OngRequestDto ongRequestDto = new OngRequestDto();
        ongRequestDto.setName(ong.getName());
        ongRequestDto.setEmail(ong.getEmail());
        ongRequestDto.setPassword(ong.getPassword());
        ongRequestDto.setCnpj(ong.getCnpj());
        ongRequestDto.setDescription(ong.getDescription());
        ongRequestDto.setCep(ong.getCep());
        ongRequestDto.setAddress(ong.getAddress());
        return ongRequestDto;
    }

    public static Ong toEntity(OngRequestDto ongRequestDto) {
        Ong ong = new Ong();
        ong.setName(ongRequestDto.getName());
        ong.setEmail(ongRequestDto.getEmail());
        ong.setPassword(ongRequestDto.getPassword());
        ong.setCnpj(ongRequestDto.getCnpj());
        ong.setDescription(ongRequestDto.getDescription());
        ong.setCep(ongRequestDto.getCep());
        ong.setAddress(ongRequestDto.getAddress());
        return ong;
    }
    
}
