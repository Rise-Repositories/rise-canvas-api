package school.sptech.crudrisecanvas.dtos;

import lombok.Data;
import school.sptech.crudrisecanvas.utils.Enums.OngStatus;

@Data
public class OngResponseNoRelationDto {
    private int id;
    private String name;
    private String cnpj;
    private String description;
    private String cep;
    private String address;
    private OngStatus status;
}