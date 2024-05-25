package school.sptech.crudrisecanvas.dtos.actionVoluntary;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryResponseDto;
import school.sptech.crudrisecanvas.dtos.action.ActionResponseDto;

@Data
    public class ActionVoluntaryDto {

        private int id;


        private ActionResponseDto action;


        private VoluntaryResponseDto voluntary;
    }
