package school.sptech.crudrisecanvas.dtos.action;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActionRequestDto {
    @NotBlank
    private String name;
    private String description;

    @FutureOrPresent
    private LocalDateTime datetimeStart;

    @Future
    private LocalDateTime datetimeEnd;

    private Double longitude;
    private Double latitude;
}