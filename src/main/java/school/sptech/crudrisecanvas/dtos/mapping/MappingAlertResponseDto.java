package school.sptech.crudrisecanvas.dtos.mapping;

import lombok.Data;
import school.sptech.crudrisecanvas.entities.Tags;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MappingAlertResponseDto {
    private Integer mappingId;
    private String address;
    private LocalDate date;
    private LocalDateTime lastServed;
    private List<Tags> tags;
}
