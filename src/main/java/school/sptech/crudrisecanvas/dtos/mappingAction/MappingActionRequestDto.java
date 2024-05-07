package school.sptech.crudrisecanvas.dtos.mappingAction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MappingActionRequestDto {
    @NotNull
    @Positive
    private Integer qtyServedPeople;
}
