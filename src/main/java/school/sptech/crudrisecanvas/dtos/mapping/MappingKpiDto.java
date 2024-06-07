package school.sptech.crudrisecanvas.dtos.mapping;

import java.time.LocalDateTime;

public interface MappingKpiDto {
    Integer getQtyTotal();
    Integer getQtyServed();
    Integer getQtyNotServed();
    Integer getQtyNoPeople();
}
