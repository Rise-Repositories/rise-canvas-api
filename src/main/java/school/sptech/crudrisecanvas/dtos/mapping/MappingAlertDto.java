package school.sptech.crudrisecanvas.dtos.mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface MappingAlertDto {
    Integer getMappingId();
    String getAddress();
    LocalDate getDate();
    LocalDateTime getLastServed();
}
