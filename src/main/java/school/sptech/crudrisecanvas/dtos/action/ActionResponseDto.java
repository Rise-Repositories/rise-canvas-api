package school.sptech.crudrisecanvas.dtos.action;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionResponseDto;
import school.sptech.crudrisecanvas.dtos.ong.OngResponseNoRelationDto;
import school.sptech.crudrisecanvas.dtos.tags.TagsResponseDto;

@Data
public class ActionResponseDto{
    private int id;
    private String name;
    private String description;
    private LocalDateTime datetimeStart;
    private LocalDateTime datetimeEnd;
    private Double longitude;
    private Double latitude;
    private Double radius;
    private String status;
    private List<MappingActionResponseDto> mappingAction;
    private OngResponseNoRelationDto ong;
    private List<TagsResponseDto> tags;
}
