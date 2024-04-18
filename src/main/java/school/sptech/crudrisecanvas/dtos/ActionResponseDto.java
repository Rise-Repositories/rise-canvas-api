package school.sptech.crudrisecanvas.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ActionResponseDto{
    private int id;
    private String name;
    private String description;
    private LocalDateTime datetimeStart;
    private LocalDateTime datetimeEnd;
    private Double longitude;
    private Double latitude;
    private OngResponseDto ong;
}
