package school.sptech.crudrisecanvas.dtos.user;


import lombok.Data;
import school.sptech.crudrisecanvas.dtos.address.AddressResponseDto;

@Data
public class UserResponseNoRelationDto {
    private Integer id;
    private String name;
    private String email;
    private String cpf;
    private AddressResponseDto address;
}
