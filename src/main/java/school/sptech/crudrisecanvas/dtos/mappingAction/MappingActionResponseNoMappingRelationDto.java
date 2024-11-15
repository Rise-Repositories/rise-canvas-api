package school.sptech.crudrisecanvas.dtos.mappingAction;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.action.ActionResponseNoMappingRelationDto;

@Data
public class MappingActionResponseNoMappingRelationDto {
    private Integer id;
    private ActionResponseNoMappingRelationDto action;
    private Integer qtyServedAdults;
    private Integer qtyServedChildren;
    private Boolean noDonation;
    private Boolean noPeople;
    private String description;
}
