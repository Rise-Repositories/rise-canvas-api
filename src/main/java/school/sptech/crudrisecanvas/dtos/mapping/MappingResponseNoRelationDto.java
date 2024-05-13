package school.sptech.crudrisecanvas.dtos.mapping;

import lombok.Data;

@Data
public class MappingResponseNoRelationDto {
    private Integer id;
    private Integer qtyAdults;
    private Integer qtyChildren;
    private Boolean hasDisorders;
    private String referencePoint;
    private String description;
    private Double latitude;
    private Double longitude;
    private String status;
    private String date;
}
