package school.sptech.crudrisecanvas.dtos.userMapping;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseDto;

@Data
public class UserMappingUserResponseDto {
    private int id;
    private MappingResponseDto mapping;
}
