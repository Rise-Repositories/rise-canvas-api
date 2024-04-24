package school.sptech.crudrisecanvas.utils.Converters;

import jakarta.persistence.AttributeConverter;
import school.sptech.crudrisecanvas.utils.Enums.MappingStatus;

public class MappingStatusConvert implements AttributeConverter<MappingStatus, String>{
    @Override
    public String convertToDatabaseColumn(MappingStatus status) {
        return status.toString();
    }

    @Override
    public MappingStatus convertToEntityAttribute(String value) {
        return MappingStatus.valueOf(value);
    }

    
}
