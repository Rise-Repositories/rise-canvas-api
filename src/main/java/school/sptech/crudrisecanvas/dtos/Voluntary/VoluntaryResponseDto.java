package school.sptech.crudrisecanvas.dtos.Voluntary;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.ong.OngResponseNoRelationDto;
import school.sptech.crudrisecanvas.dtos.user.UserResponseNoRelationDto;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

@Data
public class VoluntaryResponseDto {

    private int id;
    private VoluntaryRoles role;
    private OngResponseNoRelationDto ong;
    private UserResponseNoRelationDto user;
}
