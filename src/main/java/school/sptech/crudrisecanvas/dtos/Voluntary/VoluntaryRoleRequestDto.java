package school.sptech.crudrisecanvas.dtos.Voluntary;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

@Data
public class VoluntaryRoleRequestDto {
    @NotNull
    private VoluntaryRoles role;
}
