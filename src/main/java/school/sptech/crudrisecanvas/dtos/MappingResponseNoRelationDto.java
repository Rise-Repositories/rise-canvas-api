package school.sptech.crudrisecanvas.dtos;

import lombok.Data;

@Data
public class MappingResponseNoRelationDto {
    private Integer id;
    private Integer qtyPeople;
    private String description;
    private Double latitude;
    private Double longitude;
    private String status;
    private String date;
}
