package school.sptech.crudrisecanvas.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MappingActionRequestDto {
    @NotNull
    @Positive
    private Integer qtyServedPeople;
}
