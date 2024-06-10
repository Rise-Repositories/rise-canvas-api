package school.sptech.crudrisecanvas.dtos.mappingAction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class MappingActionRequestDto {
    @NotNull
    @PositiveOrZero
    private Integer qtyServedAdults;

    @NotNull
    @PositiveOrZero
    private Integer qtyServedChildren;

    @NotNull
    private Boolean noDonation;

    @NotNull
    private Boolean noPeople;

    private String description;
}
