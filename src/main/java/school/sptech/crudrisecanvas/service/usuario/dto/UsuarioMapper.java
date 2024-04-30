package school.sptech.crudrisecanvas.service.usuario.dto;

import school.sptech.crudrisecanvas.dtos.OngRequestDto;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.service.usuario.autenticacao.dto.UsuarioTokenDto;

public class UsuarioMapper {

    public static User toEntity(UsuarioCriacaoDto dto) {
        User entity = new User();
        entity.setEmail(dto.getEmail());
        entity.setName(dto.getName());
        entity.setPassword(dto.getPassword());
        entity.setIp(dto.getIp());
        entity.setCpf(dto.getCpf());

        return entity;
    }

    public static UsuarioTokenDto toDto(User usuario, String token) {
        UsuarioTokenDto dto = new UsuarioTokenDto();

        dto.setUserId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setNome(usuario.getName());
        dto.setToken(token);

        return dto;
    }

    public static UsuarioCriacaoDto toCricao(OngRequestDto userOng) {
        UsuarioCriacaoDto user = new UsuarioCriacaoDto();
        user.setName(userOng.getName());
        user.setEmail(userOng.getEmailUser());
        user.setPassword(userOng.getPasswordUser());
        user.setCpf(userOng.getCpfUser());

        return user;
    }
}
