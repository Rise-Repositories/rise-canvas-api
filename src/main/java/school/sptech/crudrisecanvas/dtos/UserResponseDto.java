package school.sptech.crudrisecanvas.dtos;

import java.util.List;

import lombok.Data;
import school.sptech.crudrisecanvas.entities.Mapping;

@Data
public class UserResponseDto {
    private Integer id;
    private String name;
    private String email;
    private String cpf;
    private String ip;
    private List<Mapping> mapping;
}
