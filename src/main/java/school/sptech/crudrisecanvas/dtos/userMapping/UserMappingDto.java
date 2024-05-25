package school.sptech.crudrisecanvas.dtos.userMapping;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseDto;
import school.sptech.crudrisecanvas.dtos.user.UserResponseDto;

@Data
public class UserMappingDto {

    private int id;


    private UserResponseDto user;


    private MappingResponseDto mapping;
}
