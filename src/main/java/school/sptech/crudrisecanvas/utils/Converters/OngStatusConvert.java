package school.sptech.crudrisecanvas.utils.Converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import school.sptech.crudrisecanvas.utils.Enums.OngStatus;

@Converter
public class OngStatusConvert implements AttributeConverter<OngStatus, String>{
    @Override
    public String convertToDatabaseColumn(OngStatus status) {
        return status.name();
    }

    @Override
    public OngStatus convertToEntityAttribute(String value) {
        return OngStatus.valueOf(value);
    }
}
