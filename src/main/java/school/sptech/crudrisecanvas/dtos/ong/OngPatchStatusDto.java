package school.sptech.crudrisecanvas.dtos.ong;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import school.sptech.crudrisecanvas.utils.Enums.OngStatus;

@Getter
@Setter
public class OngPatchStatusDto {
    @NotNull
    private OngStatus status;
}
