package school.sptech.crudrisecanvas.dtos.action;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.tags.TagsResponseDto;

@Data
public class ActionResponseNoRelationDto{
    private int id;
    private String name;
    private String description;
    private LocalDateTime datetimeStart;
    private LocalDateTime datetimeEnd;
    private Double longitude;
    private Double latitude;
    private Double radius;
    private String status;
    private List<TagsResponseDto> tags;
}
