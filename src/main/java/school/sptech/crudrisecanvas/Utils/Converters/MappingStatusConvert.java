package school.sptech.crudrisecanvas.Utils.Converters;

import jakarta.persistence.AttributeConverter;
import school.sptech.crudrisecanvas.Utils.Enums.MappingStatus;

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
