package school.sptech.crudrisecanvas.dtos.Voluntary;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.user.UserResponseNoRelationDto;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

@Data
public class VoluntaryOngResponseNoRelationDto {
    
    private int id;
    private VoluntaryRoles role;
    private UserResponseNoRelationDto user;
}
