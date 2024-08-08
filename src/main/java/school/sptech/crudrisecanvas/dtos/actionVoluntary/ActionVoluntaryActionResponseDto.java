package school.sptech.crudrisecanvas.dtos.actionVoluntary;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.action.ActionResponseDto;

@Data
public class ActionVoluntaryActionResponseDto {
    private int id;
    private ActionResponseDto action;
}
