package school.sptech.crudrisecanvas.dtos.ong;

import java.util.List;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryResponseNoRelationDto;
import school.sptech.crudrisecanvas.dtos.action.ActionResponseNoRelationDto;
import school.sptech.crudrisecanvas.utils.Enums.OngStatus;

@Data
public class OngResponseDto {
    private int id;
    private String name;
    private String cnpj;
    private String description;
    private String cep;
    private String address;
    private OngStatus status;
    private List<ActionResponseNoRelationDto> actions;
    private List<VoluntaryResponseNoRelationDto> voluntaries;
}
