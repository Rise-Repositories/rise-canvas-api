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
    
}
