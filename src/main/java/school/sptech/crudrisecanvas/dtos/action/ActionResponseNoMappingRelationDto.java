package school.sptech.crudrisecanvas.dtos.action;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.ong.OngResponseNoRelationDto;
import school.sptech.crudrisecanvas.dtos.tags.TagsResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActionResponseNoMappingRelationDto {
    private int id;
    private String name;
    private String description;
    private LocalDateTime datetimeStart;
    private LocalDateTime datetimeEnd;
    private Double longitude;
    private Double latitude;
    private Double radius;
    private String status;
    private OngResponseNoRelationDto ong;
    private List<TagsResponseDto> tags;
}
