package school.sptech.crudrisecanvas.dtos.actionVoluntary;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryResponseDto;
import school.sptech.crudrisecanvas.entities.Voluntary;

@Data
public class ActionVoluntaryVoluntaryResponseDto {
    private int id;
    private VoluntaryResponseDto voluntary;
}
