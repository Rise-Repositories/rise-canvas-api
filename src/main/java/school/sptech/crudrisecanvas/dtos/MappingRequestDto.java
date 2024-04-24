package school.sptech.crudrisecanvas.dtos;

import lombok.Data;
import school.sptech.crudrisecanvas.utils.Enums.MappingStatus;

@Data
public class MappingRequestDto {
    private Integer qtyPeople;
    private String description;
    private Double latitude;
    private Double longitude;
    private MappingStatus status;
}
