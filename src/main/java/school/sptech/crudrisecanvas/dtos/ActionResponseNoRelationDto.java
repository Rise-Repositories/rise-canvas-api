package school.sptech.crudrisecanvas.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ActionResponseNoRelationDto{
    private int id;
    private String name;
    private String description;
    private LocalDateTime datetimeStart;
    private LocalDateTime datetimeEnd;
    private Double longitude;
    private Double latitude;
}
