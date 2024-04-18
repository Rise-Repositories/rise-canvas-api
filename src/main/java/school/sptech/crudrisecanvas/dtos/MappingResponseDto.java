package school.sptech.crudrisecanvas.dtos;

import lombok.Data;

@Data
public class MappingResponseDto {
    private Integer id;
    private Integer qtyPeople;
    private String description;
    private Double latitute;
    private Double longitude;
    private String status;
    private String date;
    private UserResponseDto user;
}
