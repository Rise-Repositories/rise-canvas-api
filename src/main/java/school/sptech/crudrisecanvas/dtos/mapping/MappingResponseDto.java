package school.sptech.crudrisecanvas.dtos.mapping;

import java.util.List;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.user.UserResponseNoRelationDto;

@Data
public class MappingResponseDto {
    private Integer id;
    private Integer qtyPeople;
    private String description;
    private Double latitude;
    private Double longitude;
    private String status;
    private String date;
    private List<UserResponseNoRelationDto> users;
}