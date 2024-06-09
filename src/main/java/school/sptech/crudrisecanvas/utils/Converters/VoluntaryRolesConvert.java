package school.sptech.crudrisecanvas.utils.Converters;

import jakarta.persistence.AttributeConverter;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

public class VoluntaryRolesConvert implements AttributeConverter<VoluntaryRoles, String>{
    @Override
    public String convertToDatabaseColumn(VoluntaryRoles status) {
        return status.name();
    }

    @Override
    public VoluntaryRoles convertToEntityAttribute(String value) {
        return VoluntaryRoles.valueOf(value);
    }
}
