package school.sptech.crudrisecanvas.dtos;


import lombok.Data;

@Data
public class UserResponseNoRelationDto {
    private Integer id;
    private String name;
    private String email;
    private String cpf;
    private String ip;
}