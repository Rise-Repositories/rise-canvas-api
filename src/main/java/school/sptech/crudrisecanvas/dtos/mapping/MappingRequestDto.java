package school.sptech.crudrisecanvas.dtos.mapping;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import school.sptech.crudrisecanvas.dtos.address.AddressRequestDto;
import school.sptech.crudrisecanvas.entities.Address;

@Data
public class MappingRequestDto {
    @NotNull
    @Positive
    private Integer qtyAdults;

    @NotNull
    @Positive
    private Integer qtyChildren;

    private String referencePoint;

    @NotNull
    private Boolean hasDisorders;

    private String description;

    @NotNull
    @DecimalMax("90")
    @DecimalMin("-90")
    private Double latitude;

    @NotNull
    @DecimalMax("180")
    @DecimalMin("-180")
    private Double longitude;

    @NotNull
    private AddressRequestDto address;
}
