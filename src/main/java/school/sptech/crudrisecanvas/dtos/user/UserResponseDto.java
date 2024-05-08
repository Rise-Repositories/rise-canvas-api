package school.sptech.crudrisecanvas.dtos.user;

import java.util.List;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseNoRelationDto;

@Data
public class UserResponseDto {
    private Integer id;
    private String name;
    private String email;
    private String cpf;
    private String ip;
    private List<MappingResponseNoRelationDto> mapping;
}
