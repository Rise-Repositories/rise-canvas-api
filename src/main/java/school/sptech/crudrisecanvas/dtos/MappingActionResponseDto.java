package school.sptech.crudrisecanvas.dtos;

import lombok.Data;

@Data
public class MappingActionResponseDto {
    private Integer id;
    private ActionResponseNoRelationDto action;
    private MappingResponseNoRelationDto mapping;
    private int qtyServedPeople;
    
}
