package school.sptech.crudrisecanvas.dtos;

import lombok.Data;
import school.sptech.crudrisecanvas.Utils.Enums.MappingStatus;

@Data
public class MappingRequestDto {
    private Integer qtyPeople;
    private String description;
    private Double latitute;
    private Double longitude;
    private MappingStatus status;
}
