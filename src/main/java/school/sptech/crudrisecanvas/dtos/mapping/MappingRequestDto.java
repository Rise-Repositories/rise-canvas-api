package school.sptech.crudrisecanvas.dtos.mapping;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class MappingRequestDto {
    @PositiveOrZero
    private Integer qtyAdults;

    @PositiveOrZero
    private Integer qtyChildren;

    private String referencePoint;

    @NotNull
    private Boolean hasDisorders;

    private String description;

    @DecimalMax("90")
    @DecimalMin("-90")
    private Double latitude;

    @DecimalMax("180")
    @DecimalMin("-180")
    private Double longitude;
}
