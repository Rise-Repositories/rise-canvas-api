package school.sptech.crudrisecanvas.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ActionRequestDto {
    private String name;
    private String description;
    private LocalDateTime datetimeStart;
    private LocalDateTime datetimeEnd;
    private Double longitude;
    private Double latitude;
}
