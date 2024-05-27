package school.sptech.crudrisecanvas.dtos.Voluntary;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import school.sptech.crudrisecanvas.dtos.user.UserRequestDto;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

@Data
public class VoluntaryRequestDto {
    @NotNull
    private VoluntaryRoles role;

    @NotNull
    @Valid
    private UserRequestDto user;
}
