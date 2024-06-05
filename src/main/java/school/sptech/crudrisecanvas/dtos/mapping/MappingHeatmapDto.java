package school.sptech.crudrisecanvas.dtos.mapping;

import java.time.LocalDateTime;

public interface MappingHeatmapDto {
    Integer getMappingId();
    Double getLatitude();
    Double getLongitude();
    Integer getQtyAdults();
    Integer getQtyChildren();
    Boolean getNoDonation();
    LocalDateTime getDatetimeStart();
}
