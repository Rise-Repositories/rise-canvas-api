package school.sptech.crudrisecanvas.dtos.ong;

import school.sptech.crudrisecanvas.entities.Ong;

public class OngRequestMapper {
    public static Ong toEntity(OngRequestDto ongRequestDto) {
        Ong ong = new Ong();
        ong.setName(ongRequestDto.getName());
        ong.setCnpj(ongRequestDto.getCnpj());
        ong.setDescription(ongRequestDto.getDescription());
        ong.setCep(ongRequestDto.getCep());
        ong.setAddress(ongRequestDto.getAddress());
        return ong;
    }

    public static Ong toEntity(OngUpdateDto ongUpdateDto) {
        Ong ong = new Ong();
        ong.setName(ongUpdateDto.getName());
        ong.setCnpj(ongUpdateDto.getCnpj());
        ong.setDescription(ongUpdateDto.getDescription());
        ong.setCep(ongUpdateDto.getCep());
        ong.setAddress(ongUpdateDto.getAddress());
        return ong;
    }
    
}
