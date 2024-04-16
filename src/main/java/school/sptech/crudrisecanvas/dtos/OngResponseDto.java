package school.sptech.crudrisecanvas.dtos;

import lombok.Data;
import school.sptech.crudrisecanvas.Utils.Enums.OngStatus;

@Data
public class OngResponseDto {
    private int id;
    private String name;
    private String email;
    private String password;
    private String cnpj;
    private String description;
    private String cep;
    private String address;
    private OngStatus status;
}
