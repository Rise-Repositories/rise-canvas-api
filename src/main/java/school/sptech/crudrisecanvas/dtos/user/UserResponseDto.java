package school.sptech.crudrisecanvas.dtos.user;

import java.util.List;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryUserResponseDto;
import school.sptech.crudrisecanvas.dtos.address.AddressResponseDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseNoRelationDto;

@Data
public class UserResponseDto {
    private Integer id;
    private String name;
    private String email;
    private String cpf;
    private List<MappingResponseNoRelationDto> mapping;
    private List<VoluntaryUserResponseDto> voluntary;
    private AddressResponseDto address;
}
