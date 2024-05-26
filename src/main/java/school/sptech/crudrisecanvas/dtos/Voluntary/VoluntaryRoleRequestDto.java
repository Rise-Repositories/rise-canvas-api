package school.sptech.crudrisecanvas.dtos.Voluntary;

import lombok.Data;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

@Data
public class VoluntaryRoleRequestDto {
    private VoluntaryRoles role;
}
