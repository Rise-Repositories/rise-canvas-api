package school.sptech.crudrisecanvas.dtos.mappingAction;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.action.ActionResponseNoRelationDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseNoRelationDto;

@Data
public class MappingActionResponseDto {
    private Integer id;
    private ActionResponseNoRelationDto action;
    private MappingResponseNoRelationDto mapping;
    private Integer qtyServedAdults;
    private Integer qtyServedChildren;
    private Boolean noDonation;
    private Boolean noPeople;
    private String description;
}
