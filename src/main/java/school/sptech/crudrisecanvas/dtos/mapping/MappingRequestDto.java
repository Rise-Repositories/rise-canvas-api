package school.sptech.crudrisecanvas.dtos.mapping;

import lombok.Data;
import school.sptech.crudrisecanvas.utils.Enums.MappingStatus;

// TODO: need validation
@Data
public class MappingRequestDto {
    private Integer qtyPeople;
    private String description;
    private Double latitude;
    private Double longitude;
    //TODO: need remove status here, only will setted in service
    private MappingStatus status;
}
