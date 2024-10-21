package school.sptech.crudrisecanvas.dtos.userMapping;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.user.UserResponseNoRelationDto;

@Data
public class UserMappingMappingResponsDto {
    private int id;
    private UserResponseNoRelationDto user;
}
