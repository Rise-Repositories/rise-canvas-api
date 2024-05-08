package school.sptech.crudrisecanvas.dtos.action;

import java.time.LocalDateTime;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.ong.OngResponseNoRelationDto;

@Data
public class ActionResponseDto{
    private int id;
    private String name;
    private String description;
    private LocalDateTime datetimeStart;
    private LocalDateTime datetimeEnd;
    private Double longitude;
    private Double latitude;
    private OngResponseNoRelationDto ong;
}
